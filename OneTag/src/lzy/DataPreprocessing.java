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


public class DataPreprocessing {
	
	//public static void main(String[] args)  throws IOException {
		public void driver() throws IOException{	
		Map<String, Integer> map = new HashMap<String, Integer>(); 
		try {
		
			
			    File dir = new File(Config.Pre_inputFile);
				FileInputStream bigramFile = null;
				bigramFile = new FileInputStream(dir.toString());

				// Read in the file
				DataInputStream resultsStream = new DataInputStream(bigramFile);
				BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
                
				File file = new File(Config.Pre_outputFile); 
		        if (!file.exists()) {  
		            file.createNewFile();  
		        } 
		        
		        BufferedWriter writer = new BufferedWriter(new FileWriter(Config.Pre_outputFile));
				
				StringTokenizer rToken;
				String rLine;
				String text;
			
               
				// iterate through every line in the file
				while ((rLine = results.readLine()) != null) {
					rToken = new StringTokenizer(rLine," ");
					
					
					while(rToken.hasMoreTokens()){
						text = rToken.nextToken();
						
						if(text.equals("product/productId:") ){
							text = rToken.nextToken();
							writer.write(text + "\t");
							continue;
						}
						
						if(text.equals("review/userId:") ){
							text = rToken.nextToken();
							writer.write(text + "\t");
							continue;
						}
						
						
						
						if(text.equals("review/score:") ){
							text = rToken.nextToken();
							writer.write(text + "\t");
							continue;
						}
						
						
						
						
						if(text.equals("review/time:") ){
							text = rToken.nextToken();
							writer.write(text + "\t");
							continue;
						}
						
						if(text.equals("review/text:") ){
							text = rToken.nextToken();
							rLine = rLine.substring(13);
							writer.write(rLine + "\t");
							writer.newLine();
							continue;
						}
						
						
						
					}
					
				}
				
				
				if (bigramFile != null)
					bigramFile.close();
        writer.flush(); 
        writer.close();
		} catch (IOException e) {
			System.err.println("Couldn't load folder: " + Config.Pre_outputFile);
		}
	}
	
	
	


}
