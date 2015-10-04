package idea;


import filemanager.BytesToFileWriter;
import filemanager.FileGeneral;
import filemanager.FileToBytesReader;

public class IDEA {
	byte[] streamIn;
	byte[] streamOut;
	
	//byte[] Key;
	String Key;
	
	IdeaCipher cipher;
	
	String inputFilePath;
	String outputFilePath;
	
	public IDEA(String inPath, String outPath, String Key){
		this.Key = Key;
		cipher = new IdeaCipher(Key);
		FileToBytesReader reader = new FileToBytesReader();
		
		inputFilePath = inPath;
		outputFilePath = outPath;
		streamIn = reader.read(inPath);
		streamOut = new byte[streamIn.length];
		
	}
	
	public void run_encrypt() {
		byte[] clearblock = new byte[8];
		byte[] cypherblock = new byte[8];
		int numOfBlock = (streamIn.length%8 == 0) ? streamIn.length/8 : (streamIn.length/8)+1;
			System.out.println("num of block: "+numOfBlock);
		//Read file into byte array
		for (int i=0; i<numOfBlock; i++){
			int j=0;
			for (j = 0; j < 8; j++) {
				if (i*8+j < streamIn.length)
					clearblock[j] = streamIn[i*8+j];
				else break;
			}
			if(j==8)
				cipher.encrypt(clearblock, cypherblock);
			else {
				for(int k=0; k<j; k++){
					cypherblock[k]=clearblock[k];
				}
			}
			for (j = 0; j < 8; j++){
				if (i*8+j < streamIn.length)
					streamOut[i*8+j] = cypherblock[j];
				else break;
			}
		}
		
		//Write cipher text to file
		BytesToFileWriter writer = new BytesToFileWriter();
		writer.write(outputFilePath, streamOut);
		
		//Write Key to file
		FileGeneral keyStore = new FileGeneral(outputFilePath);
		String path = keyStore.getPath();
		keyStore.extensionTrim();
		keyStore.write(path, keyStore.getName()+"Key", Key);
	}
	
	public void run_decrypt() {
		byte[] clearblock = new byte[8];
		byte[] cypherblock = new byte[8];
		int numOfBlock = (streamIn.length%8 == 0) ? streamIn.length/8 : (streamIn.length/8)+1;
			System.out.println("num of block: "+numOfBlock);
		//Read file into byte array
		for (int i=0; i<numOfBlock; i++){
			int j=0;
			for (j = 0; j < 8; j++) {
				if (i*8+j < streamIn.length)
					clearblock[j] = streamIn[i*8+j];
				else break;
			}
			if(j==8)
				cipher.decrypt(clearblock, cypherblock);
			else {
				for(int k=0; k<j; k++){
					cypherblock[k]=clearblock[k];
				}
			}
			for (j = 0; j < 8; j++){
				if (i*8+j < streamIn.length)
					streamOut[i*8+j] = cypherblock[j];
				else break;
			}
		}
		
		//Write to file
		BytesToFileWriter writer = new BytesToFileWriter();
		writer.write(outputFilePath, streamOut);
	}
}
