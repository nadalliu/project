package lzy;

import java.io.BufferedReader;
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

public class Tag {
	
	//public static ArrayList<String> tagArrayList = new ArrayList<String>();
	public static ArrayList<String> tagArrayList = new ArrayList<String>();
	//public static void main(String[] args)  throws IOException {
	public void driver() throws IOException{		

		
		try {
			//map = readDirectory(new Path(args[0]));
			
			   File dir = new File(Config.rawUIR);
				FileInputStream bigramFile = null;
				bigramFile = new FileInputStream(dir.toString());

				// Read in the file
				DataInputStream resultsStream = new DataInputStream(bigramFile);
				BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
                
				
				File dir0 = new File(Config.tagOutputFile);
				FileInputStream bigramFile0 = null;
				bigramFile0 = new FileInputStream(dir0.toString());

				// Read in the file
				DataInputStream resultsStream0 = new DataInputStream(bigramFile0);
				BufferedReader results0 = new BufferedReader(new InputStreamReader(resultsStream0));
				StringTokenizer rToken0;
				String rLine0;
				String tags;
				String tagNo;
				while ((rLine0 = results0.readLine()) != null) {
					rToken0 = new StringTokenizer(rLine0);
				    tags = rToken0.nextToken();
				    tagNo = rToken0.nextToken();
				    tagArrayList.add(tags+" "+tagNo);
				} 
				if (bigramFile0 != null)
					bigramFile0.close();
				
				File file = new File(Config.uirTopic); 
		        if (!file.exists()) {  
		            file.createNewFile();  
		        }  
		        FileWriter writer = new FileWriter(file); 
		        
				
				StringTokenizer rToken;
				String rLine;
				String userID;
				String itemID;
				String rating;
				//String review;
				String tag = "";
				String reg = "'";
                int i =0;
				// iterate through every line in the file
				while ((rLine = results.readLine()) != null) {
					rLine = rLine.replaceAll(reg, " ");
					rToken = new StringTokenizer(rLine);
					// extract the meaningful information
					userID = rToken.nextToken();
					itemID = rToken.nextToken();
					rating = rToken.nextToken();
					//review = rToken.nextToken(); 
					
					//Find ONE tag for each user-item review
					
					tag = tagArrayList.get(i);
					
					
					 writer.write(userID + " " + itemID + " " + rating + " " + tag + "\r\n");
					//writer.write(tag+ "\r\n");
					 i++;
				}
				
				
				if (bigramFile != null)
					bigramFile.close();
        writer.flush(); 
        writer.close();
		} catch (IOException e) {
			System.err.println("Couldn't load folder: " + Config.rawUIR);
		}
	}
	
	
	


}
