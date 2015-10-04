package filemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileGeneral {
	private String fullPath;
	private char pathSeparator, extensionSeparator;
	
	public FileGeneral(String str) {
		fullPath = str;
		pathSeparator = '\\';
		extensionSeparator = '.';
	}
	public void set(String p) {
		fullPath = p;
	}
	public String get() {
		return fullPath;
	}
	public String getPath(){
		int sep = fullPath.lastIndexOf(pathSeparator);
		return fullPath.substring(0, sep+1);
	}
	
	public String getExtension() {
		int dot = fullPath.lastIndexOf(extensionSeparator);
	    if(dot!=-1) return fullPath.substring(dot + 1);
	    return "";
	}
	public String getName() { // gets filename without extension
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    if (dot!=-1)
	    	return fullPath.substring(sep + 1, dot);
	    return fullPath.substring(sep + 1, fullPath.length());
	  }
	public void extensionTrim(){
		int dot = fullPath.lastIndexOf(extensionSeparator);
		if (dot != -1) fullPath = fullPath.substring(0, dot);
	}
	public void write(String fpath, String fname, String content){
		try {
			File file = new File(fpath+fname);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public String read(String fpath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fpath));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	public boolean isExist(){
		File f = new File(fullPath);
		return f.exists();
	}
}
