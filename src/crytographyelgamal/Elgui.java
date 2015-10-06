package crytographyelgamal;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import crytographyelgamal.MyElgamal;


public class Elgui {
	public JPanel panel1;
	private JLabel label1;
	private JLabel label2;
	private JLabel inputPath;
	private JLabel outputPath;
	private JButton btnInputBrowse;
	private JLabel lblKey;
	private JTextField textField;
	private JButton btnRun;
	String inputpath = "";
	private int mode = 0; //0: Encrypt mode, 1: decrypt mode
	public Elgui(){
		panel1 = new JPanel();
		panel1.setBackground(SystemColor.controlLtHighlight);
		panel1.setLayout( null );

		//Input
		label1 = new JLabel( "Input file path:" );
		label1.setBounds( 10, 56, 150, 20 );
		panel1.add( label1 );

		inputPath = new JLabel();
		inputPath.setBorder(new LineBorder(Color.LIGHT_GRAY));
		inputPath.setBounds( 10, 76, 386, 20 );
		panel1.add(inputPath);
		
		btnInputBrowse = new JButton();
		btnInputBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser keyfile = new JFileChooser();
				int returnVal = keyfile.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File input  = keyfile.getSelectedFile();
					inputpath = input.getPath();
					inputPath.setText(input.getPath());
				}
			}
		});
		btnInputBrowse.setText("Browse");
		btnInputBrowse.setBounds(444, 76, 101, 20);
		panel1.add(btnInputBrowse);
		JRadioButton rdbtnEncrypt = new JRadioButton("Encrypt");
		rdbtnEncrypt.setSelected(true);
		rdbtnEncrypt.setBounds(10, 12, 109, 23);
		panel1.add(rdbtnEncrypt);
		
		JRadioButton rdbtnDecrypt = new JRadioButton("Decrypt");
		rdbtnDecrypt.setBounds(151, 12, 109, 23);
		panel1.add(rdbtnDecrypt);
		
		ButtonGroup rbGroup = new ButtonGroup();
		rbGroup.add(rdbtnEncrypt);
		rbGroup.add(rdbtnDecrypt);	
		//Output
		label2 = new JLabel( "Output file path:" );
		label2.setBounds( 10, 197, 150, 20 );
		panel1.add( label2 );

		outputPath = new JLabel();
		outputPath.setBorder(new LineBorder(Color.LIGHT_GRAY));
		outputPath.setBounds( 10, 217, 386, 20 );
		panel1.add(outputPath);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MyElgamal myElgamal = new MyElgamal(32);			
				try {
					if (rdbtnDecrypt.isSelected())
						myElgamal.decrypt(inputpath);
					else 
						myElgamal.encrypt(inputpath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		
		btnRun.setBounds(444, 214, 101, 23);
		
		panel1.add(btnRun);
		
	}
}
