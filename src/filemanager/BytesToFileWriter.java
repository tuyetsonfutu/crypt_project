package filemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BytesToFileWriter {
	public void write(String path, byte[] outByteStream){
		File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            
            // Writes bytes from the specified byte array to this file output stream
            fos.write(outByteStream);
            }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
            }
        catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
            }
        finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }

	}
}
