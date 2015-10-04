package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileToBytesReader {
	public byte[] read(String path){
	File file = new File(path);
        FileInputStream fin = null;
        try {	
            // create FileInputStream object
            fin = new FileInputStream(file);		 	
            byte fileContent[] = new byte[(int)file.length()];
            // Reads up to certain bytes of data from this input stream into an array of bytes.
	            fin.read(fileContent);
	            //create string from byte array
	            String s = new String(fileContent);
	            System.out.println("File content: " + s);
	            return fileContent;
	        }
	        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
		return null;
    }
}
