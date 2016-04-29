
package resources;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Vector;



public class FileRead {

	BufferedReader br;
	static Writer writer = null;
	

	public Vector<Double> readLines(int numberToread){

		Vector<Double> fileData = new Vector<Double>(); 

		try {
			FileInputStream fstream = new FileInputStream("input"+numberToread+".txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;         
//			int lineNumber = 0;
//			double [] a = null;
//			double [] b = null;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				fileData.add(Double.parseDouble(strLine));

			}
			// Close the input stream
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return fileData;
	}
	
	public void writePapers(String toWrite) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("Papers.txt", "UTF-8");
		writer.println(toWrite);
		writer.close();
	}
	
	
	public static void writeLines3(String toWrite, String file) throws FileNotFoundException, UnsupportedEncodingException{
	    File log = new File(file);
	    try{
	    if(log.exists()==false){
	            System.out.println("We had to make a new file.");
	            log.createNewFile();
	    }
	    PrintWriter out = new PrintWriter(new FileWriter(log, true));
	    out.println(toWrite);
	    out.close();
	    }catch(IOException e){
	        System.out.println("COULD NOT LOG!!");
	    }
		}
	
/*
	public static void main(String[] args) {

		Vector<Double> returni = new Vector<Double>();
		FileRead fileRead = new FileRead();
	//	returni.addAll(fileRead.readLines());

		for(double luku : returni) {
			System.out.println(luku);
		}
	}
*/
}

