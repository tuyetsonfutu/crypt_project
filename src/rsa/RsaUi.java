package rsa;
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

import rsa.RSA;


public class RsaUi {
	public JPanel panel = new JPanel();
	private File plant;
	private File cypher;
	JLabel outputPath ;
	JLabel lblState;
	JButton btnDecrypt;
	JButton btnEncrypt;
	double ciphersize;
	int bsize = 0 ;
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
		
		JLabel label_p = new JLabel( "P: " );
		label_p.setBounds( 10, 60 , 150, 20 );
		panel.add( label_p );
		JTextField tf_p = new JTextField(20);
		tf_p.setBounds( 10, 80, 100, 20 );
		panel.add(tf_p);
		
		JLabel label_q = new JLabel( "Q: " );
		label_q.setBounds( 150, 60 , 150, 20 );
		panel.add( label_q );
		JTextField tf_q = new JTextField(20);
		tf_q.setBounds( 150, 80, 100, 20 );
		panel.add(tf_q);
		
		JLabel label_n = new JLabel( "N: " );
		label_n.setBounds( 290, 100 , 150, 20 );
		panel.add( label_n );
		JTextField tf_n = new JTextField("");
		tf_n.setBounds( 290, 120, 100, 20 );
		panel.add(tf_n);
		
		JLabel label_pb = new JLabel( "Public key" );
		label_pb.setBounds( 10, 100 , 150, 20 );
		panel.add( label_pb );
		JTextField label_pub = new JTextField(  );
		label_pub.setBounds( 10, 120, 100, 20 );
		panel.add( label_pub );
		
		JLabel label_pr = new JLabel("Private key");
		label_pr.setBounds( 150, 100 , 150, 20 );
		panel.add( label_pr );
		JTextField label_pri = new JTextField(  );
		label_pri.setBounds( 150, 120 , 100, 20 );
		panel.add( label_pri );
		
		JButton addKey = new JButton();
		addKey.setText("Choose key");
		addKey.setBounds(444, 120, 120, 20);
		addKey.setBackground(Color.red);
		
		addKey.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser keyfile = new JFileChooser();
				int returnVal = keyfile.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//337 241 323
					File key  = keyfile.getSelectedFile();
					Scanner scanner;
					List<String> listKey = new ArrayList<String>();
					try {
						
						scanner =  new Scanner(key);
						while(scanner.hasNext()){
							String k = scanner.nextLine();
							listKey.add(k);
						}
						BigInteger pb = new BigInteger(listKey.get(0));
						BigInteger pr = new BigInteger(listKey.get(1));
						BigInteger n = new BigInteger(listKey.get(2));
						rsa = new RSA(pb,pr,n);
						label_pub.setText(rsa.getPublicKey()[1].toString());
						label_pri.setText(rsa.getPrivateKey()[1].toString());
						tf_n.setText(n.toString());
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		panel.add(addKey);
		
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
						ios = new FileInputStream(plant);
						ios.read(b);
						JOptionPane.showMessageDialog(panel, "Read file ok!" + String.valueOf(plant.length()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			}
		});
		btnInputBrowse.setText("Choose Plantext");
		btnInputBrowse.setBounds(444, 35, 120, 20);
		panel.add(btnInputBrowse);

		//Output
		JLabel label2 = new JLabel( "Rsa Cipher" );
		label2.setBounds( 10, 144, 150, 20 );
		panel.add( label2 );

		outputPath = new JLabel();
		outputPath.setBorder(new LineBorder(Color.LIGHT_GRAY));
		outputPath.setBounds( 10, 164, 386, 20 );
		panel.add(outputPath);
		
		JButton btnOutputBrowse = new JButton();
		btnOutputBrowse.setText("Choose Cipher");
		btnOutputBrowse.setBounds(444, 164, 120, 20);
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
						String g = scanner.nextLine();
						ciphersize = Double.parseDouble(g);
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
					JOptionPane.showMessageDialog(panel, "Missing Q or P!");
					return;
				}
				BigInteger p = new BigInteger(tf_p.getText());
				BigInteger q = new BigInteger(tf_q.getText());
				if(!p.isProbablePrime(1) || !q.isProbablePrime(1)){
					JOptionPane.showMessageDialog(panel, "Q or P must be prime!");
					return;
				}
				int bitsize = p.multiply(q).bitLength();
				tf_n.setText(p.multiply(q).toString());
				bsize = bitsize/2;
				rsa = new RSA(q,p,bitsize);
				label_pub.setText(rsa.getPublicKey()[1].toString());
				label_pri.setText(rsa.getPrivateKey()[1].toString());
				if(!inputPath.getText().equals("")){
					btnEncrypt.setEnabled(true);
				}
			}
		});
		btnKeygenerate.setBounds(444, 80, 120, 20);
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
				StringBuilder stringBuilder = new StringBuilder();
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
							String bytestring = Integer.toString(b[i], 2);
							while(bytestring.length() < 8 ){
								bytestring = "0"+ bytestring;
							}
							//System.out.println(b[i]);
							stringBuilder.append(bytestring);
						}
						String finalString = stringBuilder.toString();
						System.out.println(finalString);
						double strlen = finalString.length();
						output.println(strlen);
						int blockNumber = (int) (strlen /bsize);
						for(int j =0; j <= blockNumber ; j++ ){
							double begin, end;
							if ((strlen+1) <= (bsize*(j+1)) && (strlen) >= (bsize*j)){
								end =  strlen;
							}else{
								end =  bsize*(j+1);
							}
							begin = bsize*j;
							if(begin != end){
								//System.out.println(begin + " >> " + end);
								c = rsa.encrypt(finalString.substring((int)begin,(int)end));
								output.println(c);
							}
						}
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
					StringBuilder strb = new StringBuilder();
					if (file.createNewFile()){
					    int endstr = (int) (ciphersize % (rsa.getPrivateKey()[0].bitLength())/2) ;
					    System.out.println("end " + endstr);
					    for(int i =0 ; i < lenght; i++){
					    	String planstring = rsa.decript(ls.get(i));
							int m = Integer.parseInt(planstring);
							String decrypt_string = Integer.toString(m,2);
							if(lenght-1 == i && endstr !=0){
								while(decrypt_string.length() < endstr){
									decrypt_string = "0"+ decrypt_string;
								}
							}else{
								while(decrypt_string.length() < (rsa.getPrivateKey()[0].bitLength())/2){
									decrypt_string = "0"+ decrypt_string;
								}
							}
							strb.append(decrypt_string); 
							//System.out.println(decrypt_string);
						}
					    
					    String decryptString = strb.toString();
					    double delen = decryptString.length();
						int blockbyte = (int) (delen/8);
						byte [] pl = new byte[blockbyte] ;
						for(int i=0; i< blockbyte; i++ ){
							double begin, end;
							if ((delen+1) <= (8*(i+1)) && (delen) >= (8*i)){
								end =  delen;
							}else{
								end =  8*(i+1);
							}
							begin = 8*i;
							if(begin != end){
								String bytearray = decryptString.substring((int)begin,(int)end);
								byte byteValue = (byte) Integer.parseInt(bytearray, 2);
								pl[i] = byteValue;
							}
						}
						System.out.println(decryptString);
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