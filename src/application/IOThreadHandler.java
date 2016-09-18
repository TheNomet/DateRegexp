package application;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class IOThreadHandler extends Thread {
	
	private StringBuilder output = new StringBuilder();
	
	private InputStream inputStream;
	
	public IOThreadHandler(InputStream inputStream) {
		this.inputStream=inputStream;
	}
	
	public void run(){
		Scanner br = null;
		
		try{
			
			br = new Scanner (new InputStreamReader (inputStream));
			String line = null;
			while(br.hasNextLine()){
				line=br.nextLine();
				output.append(line+System.getProperty("line.separator"));
			}
			
		} finally{
			br.close();
		}
	}
	
	public StringBuilder getOutput(){
		return output;
	}
}
