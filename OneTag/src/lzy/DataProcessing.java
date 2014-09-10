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

public class DataProcessing {


	//public static ArrayList<String> tagArrayList = new ArrayList<String>();
	
	//public static void main(String[] args)  throws IOException {
	public void driver() throws IOException{		

		//System.out.println("input path: " + Config.stemmedInputFile); // Config.stemmedOutputFile   initial Review  N:/Project/ReviewTextsWithStemming/MidoutTag.txt
		

		Map<String, Integer> map = new HashMap<String, Integer>(); 
		try {
			//map = readDirectory(new Path(args[0]));
			
			    File dir = new File(Config.stemmedInputFile); //Config.stemmedOutputFile 
				FileInputStream bigramFile = null;
				bigramFile = new FileInputStream(dir.toString());

				// Read in the file
				DataInputStream resultsStream = new DataInputStream(bigramFile);
				BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
                
				
				StringTokenizer rToken;
				String rLine;
				String text;
				String reg = "'";
                int i = 1;
				// iterate through every line in the file
				while ((rLine = results.readLine()) != null) {
					rLine = rLine.replaceAll(reg, " ");
					rToken = new StringTokenizer(rLine);
					
					 File file = new File(Config.seperateReivewFile + i + ".txt");   //N:/Project/ReviewTexts/
				        if (!file.exists()) {  
				            file.createNewFile();  
				        }  
					 BufferedWriter writer = new BufferedWriter(new FileWriter(Config.seperateReivewFile + i + ".txt")); //N:/Project/ReviewTexts/
					 
					
					while(rToken.hasMoreTokens()){
						text = rToken.nextToken();
						 writer.write(text + " ");
					}
					    writer.flush(); 
				        writer.close();
				        i++; 
					
				}
				
				
				if (bigramFile != null)
					bigramFile.close();
       // writer.flush(); 
        //writer.close();
		} catch (IOException e) {
			System.err.println("Couldn't load folder: " + Config.stemmedInputFile); //Config.stemmedOutputFile
		}
	}
	
	
	


}
