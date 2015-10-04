import idea.RSA;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


public class RsaUi {
	public JPanel panel = new JPanel();
	private File plant;
	private File cypher;
	JLabel outputPath ;
	JLabel lblState;
	JButton btnDecrypt;
	JButton btnEncrypt;
	List<String> ls;
	RSA rsa;
	byte [] b;
	public RsaUi(){
		panel = new JPanel();
		panel.setBackground(SystemColor.controlLtHighlight);
		panel.setLayout( null );

		//Input
		JLabel label1 = new JLabel( "RSA Plant" );
		label1.setBounds( 10, 15, 150, 20 );
		panel.add( label1 );
		
		JLabel label_p = new JLabel( "P = " );
		label_p.setBounds( 10, 60 , 150, 20 );
		panel.add( label_p );
		JTextField tf_p = new JTextField(20);
		tf_p.setBounds( 10, 80, 100, 20 );
		panel.add(tf_p);
		
		JLabel label_q = new JLabel( "Q = " );
		label_q.setBounds( 150, 60 , 150, 20 );
		panel.add( label_q );
		JTextField tf_q = new JTextField(20);
		tf_q.setBounds( 150, 80, 100, 20 );
		panel.add(tf_q);
		
//		JLabel label_e = new JLabel( "E = " );
//		label_e.setBounds( 300, 60 , 100, 20 );
//		panel.add( label_e );
		
//		JTextField tf_e = new JTextField(20);
//		tf_e.setBounds( 300, 80 , 100, 20 );
//		tf_e.setText("2");
//		tf_e.disable();
//		panel.add(tf_e);
		
		JLabel label_pub = new JLabel( "Public key" );
		label_pub.setBounds( 10, 120, 150, 20 );
		panel.add( label_pub );
		
		JLabel label_pri = new JLabel( "Private key" );
		label_pri.setBounds( 200, 120 , 150, 20 );
		panel.add( label_pri );
		
		
		
		JLabel inputPath = new JLabel();
		inputPath.setBorder(new LineBorder(Color.LIGHT_GRAY));
		inputPath.setBounds( 10, 35, 386, 20 );
		panel.add(inputPath);
		
		JButton btnInputBrowse = new JButton();
		btnInputBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser input_path = new JFileChooser();
				int returnVal = input_path.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					plant = input_path.getSelectedFile();
					inputPath.setText(plant.getName());
					lblState.setText("Ready");
					InputStream ios = null;
					b = new byte[(int) plant.length()];
					try {
						//byte[] data = Files.readAllBytes(plant.toPath());
						ios = new FileInputStream(plant);
						ios.read(b);
						JOptionPane.showMessageDialog(panel, "File already exists." + String.valueOf(plant.length()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			}
		});
		btnInputBrowse.setText("Choose Plantext");
		btnInputBrowse.setBounds(444, 35, 150, 20);
		panel.add(btnInputBrowse);

		//Output
		JLabel label2 = new JLabel( "Rsa Cypher" );
		label2.setBounds( 10, 144, 150, 20 );
		panel.add( label2 );

		outputPath = new JLabel();
		outputPath.setBorder(new LineBorder(Color.LIGHT_GRAY));
		outputPath.setBounds( 10, 164, 386, 20 );
		panel.add(outputPath);
		
		JButton btnOutputBrowse = new JButton();
		btnOutputBrowse.setText("Choose Cypher");
		btnOutputBrowse.setBounds(444, 164, 150, 20);
		btnOutputBrowse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser cypher_file = new JFileChooser();
				int returnVal = cypher_file.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					cypher  = cypher_file.getSelectedFile();
					Scanner scanner;
					try {
						scanner = new Scanner(cypher);
						ls = new ArrayList<String>() ;
						
						while(scanner.hasNext()){
						   ls.add(scanner.nextLine());  
						}
						outputPath.setText(cypher.toPath().toString());
						System.out.println("Readfile ok");
						btnDecrypt.setEnabled(true);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		panel.add(btnOutputBrowse);
		
		JButton btnKeygenerate = new JButton();
		btnKeygenerate.setText("Generate key");
		btnKeygenerate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (tf_p.getText().equals("") || tf_q.getText().equals("")){
					JOptionPane.showMessageDialog(panel, "Missing q or p!");
					return;
				}
				BigInteger p = new BigInteger(tf_p.getText());
				BigInteger q = new BigInteger(tf_q.getText());
				if(!p.isProbablePrime(1) || !q.isProbablePrime(1)){
					JOptionPane.showMessageDialog(panel, "q or q is not prime!");
					return;
				}
				rsa = new RSA(q,p);
				label_pub.setText("PbK "+rsa.getPublicKey()[1].toString());
				label_pri.setText("PrK "+rsa.getPrivateKey()[1].toString());
				if(!inputPath.getText().equals("")){
					btnEncrypt.setEnabled(true);
				}
			}
		});
		btnKeygenerate.setBounds(444, 100, 150, 20);
		panel.add(btnKeygenerate);
		
		
		btnEncrypt = new JButton("RSA Encrypt");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnEncrypt.setBounds(10, 207, 89, 23);
		if(!inputPath.getText().equals(""))
			btnEncrypt.setEnabled(false);
		btnEncrypt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String c = "";
				System.out.print(b);
				if (b.length == 0){
					JOptionPane.showMessageDialog(panel, "File not found!");
					return;
				}
				if (rsa.getPrivateKey() == null){
					JOptionPane.showMessageDialog(panel, "Invalid key");
					return;
				}
				try {
				      File file = new File("f://"+inputPath.getText()+".cipher");
				      if (file.createNewFile()){
				        PrintWriter output = new PrintWriter(file);
						for (int i =0; i < b.length; i++){
							c = rsa.encrypt(b[i]);
							output.println(c);
						}
						System.out.println();
						output.flush();
						output.close();
						JOptionPane.showMessageDialog(panel, "Done " + "f://"+inputPath.getText()+".cipher");
				      }else{
				    	JOptionPane.showMessageDialog(panel, "File already exists.");
				      }
				      
			    	} catch (Exception e) {
				      e.printStackTrace();
				}
			}
		});
		panel.add(btnEncrypt);
		
		btnDecrypt = new JButton("RSA Decrypt");
		btnDecrypt.setEnabled(false);
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tf_p.getText().equals("") || tf_q.getText().equals("")){
					JOptionPane.showMessageDialog(panel, "Missing q or p!");
					return;
				}
				BigInteger p = new BigInteger(tf_p.getText());
				BigInteger q = new BigInteger(tf_q.getText());
				if(!p.isProbablePrime(1) || !q.isProbablePrime(1)){
					JOptionPane.showMessageDialog(panel, "q or q is not prime!");
					return;
				}
				int lenght = ls.size();
				int name_size = outputPath.getText().length() - 7 ;
				String name_file = outputPath.getText().substring(0,name_size);
				String tail_file = outputPath.getText().substring(name_size,outputPath.getText().length());
				if(!tail_file.equals(".cipher")){
					System.out.print("It is not cipher file");
					return;
				}
				File file = new File(name_file);
				try {
					if (file.createNewFile()){
					    byte [] pl = new byte[lenght] ;
					    for(int i =0 ; i<lenght; i++){
							int m = Integer.parseInt(rsa.decript(ls.get(i)));
							pl[i] =(byte)m;
							
						}
					    FileOutputStream fos = new FileOutputStream(file);
						fos.write(pl);
						fos.close();
						JOptionPane.showMessageDialog(panel, "Done " + "f://"+inputPath.getText()+".cipher");
					  }else{
						JOptionPane.showMessageDialog(panel, "File already exists.");
					  }
				} catch (HeadlessException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnDecrypt.setBounds(128, 207, 89, 23);
		if (!inputPath.getText().equals("")){
			btnEncrypt.setEnabled(true);
		}
		else {
			btnEncrypt.setEnabled(false);
		}
		panel.add(btnDecrypt);
		
		
		lblState = new JLabel("");
		lblState.setBounds(10, 276, 592, 14);
		if (!inputPath.getText().equals("")){
			lblState.setText("Ready");
			btnEncrypt.setEnabled(true);
		}
		else {
			lblState.setText("Choose file to encrypt");
			btnEncrypt.setEnabled(false);
		}
		panel.add(lblState);
		
	}
}