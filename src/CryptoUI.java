import filemanager.FileGeneral;
import idea.IDEA;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JTextField;

public class CryptoUI {

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel panel1;
	private JLabel label1;
	private JLabel label2;
	private JLabel inputPath;
	private JLabel outputPath;
	private JButton btnInputBrowse;
	private JTextArea lblState;
	private JLabel lblKey;
	private JButton btnFromFile;
	private JTextField textField;
	
	private JRadioButton rdbtnEncrypt;
	private JRadioButton rdbtnDecrypt;
	private JButton btnRun;
	
	private int mode = 0; //0: Encrypt mode, 1: decrypt mode
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CryptoUI window = new CryptoUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CryptoUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBackground(SystemColor.activeCaption);
		frame.setBounds(350, 200, 623, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Crypto Demo");
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		
		frame.getContentPane().add( topPanel );

		// Create the tab pages
		createPage1();
		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(SystemColor.controlShadow);
		tabbedPane.addTab( "IDEA", panel1 );
		// Create the tab page
		RsaUi rsatab = new RsaUi();
		tabbedPane.addTab( "RSA", rsatab.panel );
		Elgui elgtab = new Elgui();
		tabbedPane.addTab( "Elgamal", elgtab.panel1 );
		topPanel.add( tabbedPane );
		
		
		
		
		
		topPanel.add( tabbedPane );
	}

	public void createPage1()
	{
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
				chooseAndSetPath();
			}
		});
		btnInputBrowse.setText("Browse");
		btnInputBrowse.setBounds(444, 76, 101, 20);
		panel1.add(btnInputBrowse);

		//Key
		lblKey = new JLabel("Key:");
		lblKey.setBounds(10, 107, 150, 20);
		panel1.add(lblKey);
		
		textField = new JTextField();
		textField.setBounds(10, 127, 386, 20);
		panel1.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateKey();
			}
		});
		btnNewButton.setBounds(10, 158, 101, 23);
		panel1.add(btnNewButton);
		
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
				runActionPerformed();
			}
		});
		
		btnRun.setBounds(444, 214, 101, 23);
		
		panel1.add(btnRun);
		
		btnFromFile = new JButton("From file");
		btnFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					keyFromFileActionPerformed();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnFromFile.setBounds(121, 158, 101, 23);
		panel1.add(btnFromFile);
		
		
		lblState = new JTextArea();
		lblState.setBounds(10, 276, 592, 14);
		lblState.setForeground(new Color(127, 131,133));
		if (!inputPath.getText().equals("") && !outputPath.getText().equals("") && !textField.getText().equals(""))
			lblState.setText("Ready");
		else{ 
			if(inputPath.getText().equals("")) lblState.append("No file input. ");
			if(textField.getText().equals("")) lblState.append("No Key determined. ");
		}
		panel1.add(lblState);
		
		//radio button group
		rdbtnEncrypt = new JRadioButton("Encrypt");
		rdbtnEncrypt.setSelected(true);
		rdbtnEncrypt.setBounds(10, 12, 109, 23);
		rdbtnEncrypt.setActionCommand("enc");
		rdbtnEncrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(rdbtnEncrypt.isSelected()) mode = 0;
				inputPath.setText("");
				outputPath.setText("");
				
			}
		});
		panel1.add(rdbtnEncrypt);
		
		rdbtnDecrypt = new JRadioButton("Decrypt");
		rdbtnDecrypt.setBounds(151, 12, 109, 23);
		rdbtnDecrypt.setActionCommand("dec");
		rdbtnDecrypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(rdbtnDecrypt.isSelected()) mode = 1;
				inputPath.setText("");
				outputPath.setText("");
			}
		});
		panel1.add(rdbtnDecrypt);
		
		ButtonGroup rbGroup = new ButtonGroup();
		rbGroup.add(rdbtnEncrypt);
		rbGroup.add(rdbtnDecrypt);		
	}
	
	void chooseAndSetPath(){
		String path = "";
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(mode==1) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Ciphertext files", "idea", "IDEA");
			filechooser.setFileFilter(filter);
		}
		if(filechooser.showDialog(btnInputBrowse, "OK") == JFileChooser.APPROVE_OPTION) {
			path = filechooser.getSelectedFile().getAbsolutePath();
			inputPath.setText(path);
			//check if mode = 0, set output to file .idea
			if (mode == 0) outputPath.setText(path+".idea");
			else {
				FileGeneral temp = new FileGeneral(path);
				temp.extensionTrim();
				//Check if outout filename is exists
				int count=0;
				while(temp.isExist()){
					count++;
				}
				temp.set(temp.getPath() + temp.getName() +"(" + Integer.toString(count)+")." +  temp.getExtension());
				outputPath.setText(temp.get());
			}
			if (!inputPath.getText().equals("") && !outputPath.getText().equals("") && !textField.getText().equals(""))
				lblState.setText("Ready");
			else{ 
				lblState.setText("");
				if(inputPath.getText().equals("")) lblState.append("No file input. ");
				if(textField.getText().equals("")) lblState.append("No Key determined. ");
			}
		}
	}
	
	void encryptActionPerformed(){
		IDEA ideaCrypto = new IDEA(inputPath.getText(), outputPath.getText(), textField.getText());
		ideaCrypto.run_encrypt();
	}
	void decryptActionPerformed(){
		IDEA ideaCrypto = new IDEA(inputPath.getText(), outputPath.getText(), textField.getText());
		ideaCrypto.run_decrypt();
	}
	void runActionPerformed(){
		//String a = rdbtnDecrypt.isSelected();
		if(inputPath.getText().equals("") || outputPath.getText().equals("") || textField.getText().equals("")){
			String message = "Input file or Key haven't determined" ;
			JFrame errFrame = new JFrame();
			errFrame.setBounds(400, 350, 60, 60);
			    JOptionPane.showMessageDialog(errFrame, message, "Lack of information!",
			        JOptionPane.ERROR_MESSAGE);
			  return;
		}
		if (rdbtnDecrypt.isSelected())
			decryptActionPerformed();
		else 
			encryptActionPerformed();
			
	}
	void generateKey(){
		String key = "";
		byte[] block = new byte[16];
		for(int i=0; i<16; i++){
			block[i] = (byte) ( Math.random() * 256.0 );
			String temp = "";
			for(int j = 0; j < 8; j++){
				if (block[i]%2==1) temp = "1"+temp;
				else temp = "0"+temp;
				block[i] >>= 1;
			}
			key = key+temp;
		}
		textField.setText(key);
		if (!inputPath.getText().equals("") && !outputPath.getText().equals("") && !textField.getText().equals(""))
			lblState.setText("Ready");
		else{ 
			lblState.setText("");
			if(inputPath.getText().equals("")) lblState.append("No file input. ");
			if(textField.getText().equals("")) lblState.append("No Key determined. ");
		}
	}
	void keyFromFileActionPerformed() throws IOException{
		String path = "";
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(filechooser.showDialog(btnInputBrowse, "OK") == JFileChooser.APPROVE_OPTION) {
			path = filechooser.getSelectedFile().getAbsolutePath();
		}
		if (!inputPath.getText().equals("") && !outputPath.getText().equals("") && !textField.getText().equals(""))
			lblState.setText("Ready");
		else{ 
			lblState.setText("");
			if(inputPath.getText().equals("")) lblState.append("No file input. ");
			if(textField.getText().equals("")) lblState.append("No Key determined. ");
		}
		FileGeneral file = new FileGeneral(path);
		textField.setText(file.read(path));
	}
}
