package lzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter;  
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import config.Config;


public class firstDataProcessing {


	//public static ArrayList<String> tagArrayList = new ArrayList<String>();
	
//	public static void main(String[] args)  throws IOException {
	public void driver() throws IOException{		
		Map<String, Integer> map = new HashMap<String, Integer>(); 
		try {
			//map = readDirectory(new Path(args[0]));
			
			    File dir = new File(Config.stemmedInputFile);
				FileInputStream bigramFile = null;
				bigramFile = new FileInputStream(dir.toString());

				// Read in the file
				DataInputStream resultsStream = new DataInputStream(bigramFile);
				BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
                
				File file = new File(Config.stemmedOutputFile); 
		        if (!file.exists()) {  
		            file.createNewFile();  
		        }  
		       // FileWriter writer = new FileWriter(file); 
		        
		        BufferedWriter writer = new BufferedWriter(new FileWriter(Config.stemmedOutputFile));
				
				StringTokenizer rToken;
				String rLine;
				String text;
				String userID;
				String itemID;
				String rating;
				//String review;
				String tag = "";
				String reg = "'";
               
				// iterate through every line in the file
				while ((rLine = results.readLine()) != null) {
					rLine = rLine.replaceAll(reg, " ");
					rToken = new StringTokenizer(rLine);
					
					
					while(rToken.hasMoreTokens()){
						text = rToken.nextToken();
						
						if(text.equals("**********") ){
							text = "";
							writer.newLine();
							continue;
						}
						
						 writer.write(text + " ");
					}
					
				}
				
				
				if (bigramFile != null)
					bigramFile.close();
        writer.flush(); 
        writer.close();
		} catch (IOException e) {
			System.err.println("Couldn't load folder: " + Config.stemmedInputFile);
		}
	}
	
	
	


}
