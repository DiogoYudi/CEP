package cep;

import java.awt.Cursor;
import java.awt.SystemColor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import Atxy2k.CustomTextField.RestrictedTextField;

public class Cep extends JFrame{
	private JPanel contentPane;
	private JTextField txtCep;
	private JTextField txtAddress;
	private JTextField txtBairro;
	private JTextField txtCity;
	@SuppressWarnings("rawtypes")
	private JComboBox cboUf;
	private JLabel lblStatus;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run(){
                try {
                    Cep frame = new Cep();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Cep(){
        setTitle("Buscar CEP");
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Cep.class.getResource("/img/home.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

		//LABEL
        JLabel lblCep = new JLabel("CEP");
		lblCep.setBounds(23, 38, 46, 14);
		contentPane.add(lblCep);

		JLabel lblAddress = new JLabel("Endereço");
		lblAddress.setBounds(10, 78, 59, 14);
		contentPane.add(lblAddress);

		JLabel lblBairro = new JLabel("Bairro");
		lblBairro.setBounds(23, 120, 46, 14);
		contentPane.add(lblBairro);

		JLabel lblCity = new JLabel("Cidade");
		lblCity.setBounds(23, 165, 46, 14);
		contentPane.add(lblCity);

		JLabel lblUF = new JLabel("UF");
		lblUF.setBounds(315, 165, 20, 14);
		contentPane.add(lblUF);

		//TEXTFIELD
		txtCep = new JTextField();
		txtCep.setBounds(61, 35, 104, 20);
		contentPane.add(txtCep);
		txtCep.setColumns(10);

		txtAddress = new JTextField();
		txtAddress.setBounds(79, 75, 325, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);

		txtBairro = new JTextField();
		txtBairro.setBounds(79, 117, 325, 20);
		contentPane.add(txtBairro);
		txtBairro.setColumns(10);

		txtCity = new JTextField();
		txtCity.setBounds(79, 162, 218, 20);
		contentPane.add(txtCity);
		txtCity.setColumns(10);

		//COMBOBOX
        cboUf = new JComboBox();
		cboUf.setModel(new DefaultComboBoxModel(
				new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
						"PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
		cboUf.setBounds(345, 161, 59, 22);
		contentPane.add(cboUf);

		//BUTTON
        JButton btnClean = new JButton("Limpar");
		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btnClean.setBounds(23, 214, 89, 23);
		contentPane.add(btnClean);

		JButton btnCep = new JButton("Buscar");
		btnCep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(txtCep.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Informe o CEP");
					txtCep.requestFocus();
				}else{
					buscarCep();
				}
			}
		});
		btnCep.setBounds(200, 33, 89, 23);
		contentPane.add(btnCep);

        JButton btnAbout = new JButton("");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Sobre sobre = new Sobre();
				sobre.setVisible(true);
			}
		});
		btnAbout.setToolTipText("Sobre");
		btnAbout.setIcon(new ImageIcon(Cep.class.getResource("/img/about.png")));
		btnAbout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAbout.setBorder(null);
		btnAbout.setBackground(SystemColor.control);
		btnAbout.setBounds(356, 11, 48, 48);
		contentPane.add(btnAbout);

		lblStatus = new JLabel("");
		lblStatus.setBounds(170, 35, 20, 20);
		contentPane.add(lblStatus);

		RestrictedTextField validar = new RestrictedTextField(txtCep);
		validar.setOnlyNums(true);
		validar.setLimit(8);
    }

	private void buscarCep(){
		String logradouro = "";
		String type = "";
		String result = null;
		String cep = txtCep.getText();
		try {
			URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep="+cep+"&formato=xml");
			SAXReader xml = new SAXReader();
			Document documento = xml.read(url);
			Element root = documento.getRootElement();
			boolean cepEncontrado = false;
			for(Iterator<Element> it = root.elementIterator(); it.hasNext();){
				Element element = it.next();
				if(element.getQualifiedName().equals("cidade")) txtCity.setText(element.getText());
				if(element.getQualifiedName().equals("bairro")) txtBairro.setText(element.getText());
				if(element.getQualifiedName().equals("uf")) cboUf.setSelectedItem(element.getText());
				if(element.getQualifiedName().equals("tipo_logradouro")) type = element.getText();
				if(element.getQualifiedName().equals("logradouro")) logradouro = element.getText();
				if(element.getQualifiedName().equals("resultado")){
					result = element.getText();
					if(!result.equals("0")){
						lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/check.png")));
						cepEncontrado = true;
					}
				}
			}
			if(cepEncontrado) txtAddress.setText(type + " " + logradouro);
			else{
				txtAddress.setText(null);
				JOptionPane.showMessageDialog(null, "CEP não encontrado");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void limpar() {
		txtCep.setText(null);
		txtAddress.setText(null);
		txtBairro.setText(null);
		txtCity.setText(null);
		cboUf.setSelectedItem(null);
		txtCep.requestFocus();
		lblStatus.setIcon(null);
	}
}
