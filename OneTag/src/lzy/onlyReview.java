package lzy;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import lzy.Stopwords;
import config.Config;

public class onlyReview{
	
	
	/*public static ArrayList<String> TopicList10 = new ArrayList<String>();*/
	
	//public static <eachReview> void main(String[] args)  throws IOException {
	//public static  void main(String[] args)  throws IOException {	
	public void driver() throws IOException{	
		ArrayList<String> TopicList0 = new ArrayList<String>();	
		ArrayList<String> TopicList1 = new ArrayList<String>();
		ArrayList<String> TopicList2 = new ArrayList<String>();
		ArrayList<String> TopicList3 = new ArrayList<String>();
		ArrayList<String> TopicList4 = new ArrayList<String>();
		ArrayList<String> TopicList5 = new ArrayList<String>();
		ArrayList<String> TopicList6 = new ArrayList<String>();
		ArrayList<String> TopicList7 = new ArrayList<String>();
		ArrayList<String> TopicList8 = new ArrayList<String>();
		ArrayList<String> TopicList9 = new ArrayList<String>();
	/*
     * put topic list into different Array List
     */
	try {
		//map = readDirectory(new Path(args[0]));
		
		    File dir = new File(Config.optimalTag_LDA);//topic input: "N:/Project/Tag_LDA/lda_2000.twords"
			FileInputStream f = null;
			f = new FileInputStream(dir.toString());

			// Read in the file
			DataInputStream resultsStream = new DataInputStream(f);
			BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
			
			StringTokenizer rToken;
			String rLine;
			String reg = ":";
            int i=0;
			// iterate through every line in the file
			while ((rLine = results.readLine()) != null) {
				rLine = rLine.replaceAll(reg, " ");
				rToken = new StringTokenizer(rLine);
				String title = rToken.nextToken().toLowerCase(); //"TOPIC"
				String topicNum = rToken.nextToken().toLowerCase();
				// extract the meaningful information
				while(rToken.hasMoreTokens()){
					
					String topic = rToken.nextToken().toLowerCase();
					
					switch(i){
					case 0 :TopicList0.add(topic); break;
					case 1 :TopicList1.add(topic); break;
					case 2 :TopicList2.add(topic); break;
					case 3 :TopicList3.add(topic); break;
					case 4 :TopicList4.add(topic); break;
					case 5 :TopicList5.add(topic); break;
					case 6 :TopicList6.add(topic); break;
					case 7 :TopicList7.add(topic); break;
					case 8 :TopicList8.add(topic); break;
					case 9 :TopicList9.add(topic); break;
					//case 10 :TopicList10.add(topic); break;
					//case 11 :TopicList11.add(topic); break;
					//case 12 :TopicList12.add(topic); break;
					//case 13 :TopicList13.add(topic); break;
					//case 14 :TopicList14.add(topic); break;
					//case 15 :TopicList15.add(topic); break;
					//case 16 :TopicList16.add(topic); break;
					//case 17 :TopicList17.add(topic); break;
					//case 18 :TopicList18.add(topic); break;
					//case 19 :TopicList19.add(topic); break;
					//case 20 :TopicList20.add(topic); break;
					//case 21 :TopicList21.add(topic); break;
					//case 22 :TopicList22.add(topic); break;
					//case 23 :TopicList23.add(topic); break;
					//case 24 :TopicList24.add(topic); break;
					//case 25 :TopicList25.add(topic); break;
					//case 26 :TopicList26.add(topic); break;
					//case 27 :TopicList27.add(topic); break;
					//case 28 :TopicList28.add(topic); break;
					//case 29 :TopicList29.add(topic); break;
					
					
					}
				}
				i++;	
				
			}
			//System.out.println(TopicList27.get(10));
			if (f != null)
				f.close();
  
	} catch (IOException e) {
		System.err.println("Couldn't load folder: topic input");
	} 

    /*
     * find tag for each review
     */
	Map<String, Integer> topic = new HashMap<String, Integer>();
	try {
		
		
		    File dir = new File(Config.stemmedInputFile); //input review  yelp_review  MidoutTag
			FileInputStream bigramFile = null;
			bigramFile = new FileInputStream(dir.toString());
			
			File file = new File(Config.tagOutputFile); //output path  outTag
	        if (!file.exists()) {  
	            file.createNewFile();  
	        }  
			FileWriter writer = new FileWriter(file);
		

			// Read in the file
			DataInputStream resultsStream = new DataInputStream(bigramFile);
			BufferedReader results = new BufferedReader(new InputStreamReader(resultsStream));
            
			
			StringTokenizer rToken;
			String rLine;
			String reg = "\\W";
			String zero = "0";
			 
			//int topicNum = 30;
		   while ((rLine = results.readLine()) != null) {
			   
			   for(int t = 0; t<Config.topicNum; t++){
				   String key = "TOPIC " + t;
				   topic.put(key, 0); 
			   }
		       /*
				topic.put("TOPIC 0", 0);
				
				*/
				List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(topic.entrySet());
				rLine = rLine.replaceAll(reg, " ");
				rToken = new StringTokenizer(rLine);
				// extract the meaningful information
				while(rToken.hasMoreTokens()){
					String review = rToken.nextToken().toLowerCase();
					//Remove stop words 
					if(Stopwords.isStopword(review))
					   continue;
					
					// word stemming
					
					//Find ONE tag for each user-item review
					for(int j=0;j<TopicList0.size();j++){
						if(review.equals(TopicList0.get(j))){
							topic.put("TOPIC 0", topic.get("TOPIC 0") + 1);
							//System.out.println(topic.get("TOPIC 0"));
							break;
						}
					}
					for(int j=0;j<TopicList1.size();j++){
						if(review.equals(TopicList1.get(j))){
							topic.put("TOPIC 1", topic.get("TOPIC 1") + 1);
							break;
						}
					}
					for(int j=0;j<TopicList2.size();j++){
						if(review.equals(TopicList2.get(j))){
							topic.put("TOPIC 2", topic.get("TOPIC 2") + 1);
							break;
						}
					}
					for(int j=0;j<TopicList3.size();j++){
						if(review.equals(TopicList3.get(j))){
							topic.put("TOPIC 3", topic.get("TOPIC 3") + 1);
							break;
						}
					}for(int j=0;j<TopicList4.size();j++){
						if(review.equals(TopicList4.get(j))){
							topic.put("TOPIC 4", topic.get("TOPIC 4") + 1);
							break;
						}
					}for(int j=0;j<TopicList5.size();j++){
						if(review.equals(TopicList5.get(j))){
							topic.put("TOPIC 5", topic.get("TOPIC 5") + 1);
							break;
						}
					}for(int j=0;j<TopicList6.size();j++){
						if(review.equals(TopicList6.get(j))){
							topic.put("TOPIC 6", topic.get("TOPIC 6") + 1);
							break;
						}
					}for(int j=0;j<TopicList7.size();j++){
						if(review.equals(TopicList7.get(j))){
							topic.put("TOPIC 7", topic.get("TOPIC 7") + 1);
							break;
						}
					}for(int j=0;j<TopicList8.size();j++){
						if(review.equals(TopicList8.get(j))){
							topic.put("TOPIC 8", topic.get("TOPIC 8") + 1);
							break;
						}
					}for(int j=0;j<TopicList9.size();j++){
						if(review.equals(TopicList9.get(j))){
							topic.put("TOPIC 9", topic.get("TOPIC 9") + 1);
							break;
						}
					}
						/*for(int j=0;j<TopicList10.size();j++){
							if(review.equals(TopicList10.get(j))){
								topic.put("TOPIC 10", topic.get("TOPIC 10") + 1);
								//System.out.println(topic.get("TOPIC 0"));
								break;
							}
						}
						for(int j=0;j<TopicList11.size();j++){
							if(review.equals(TopicList11.get(j))){
								topic.put("TOPIC 11", topic.get("TOPIC 11") + 1);
								break;
							}
						}
						for(int j=0;j<TopicList12.size();j++){
							if(review.equals(TopicList12.get(j))){
								topic.put("TOPIC 12", topic.get("TOPIC 12") + 1);
								break;
							}
						}
						for(int j=0;j<TopicList13.size();j++){
							if(review.equals(TopicList13.get(j))){
								topic.put("TOPIC 13", topic.get("TOPIC 13") + 1);
								break;
							}
						}for(int j=0;j<TopicList14.size();j++){
							if(review.equals(TopicList14.get(j))){
								topic.put("TOPIC 14", topic.get("TOPIC 14") + 1);
								break;
							}
						}for(int j=0;j<TopicList15.size();j++){
							if(review.equals(TopicList15.get(j))){
								topic.put("TOPIC 15", topic.get("TOPIC 15") + 1);
								break;
							}
						}for(int j=0;j<TopicList16.size();j++){
							if(review.equals(TopicList16.get(j))){
								topic.put("TOPIC 16", topic.get("TOPIC 16") + 1);
								break;
							}
						}for(int j=0;j<TopicList17.size();j++){
							if(review.equals(TopicList17.get(j))){
								topic.put("TOPIC 17", topic.get("TOPIC 17") + 1);
								break;
							}
						}for(int j=0;j<TopicList18.size();j++){
							if(review.equals(TopicList18.get(j))){
								topic.put("TOPIC 18", topic.get("TOPIC 18") + 1);
								break;
							}
						}for(int j=0;j<TopicList19.size();j++){
							if(review.equals(TopicList19.get(j))){
								topic.put("TOPIC 19", topic.get("TOPIC 19") + 1);
								break;
							}
						}
							for(int j=0;j<TopicList20.size();j++){
								if(review.equals(TopicList20.get(j))){
									topic.put("TOPIC 20", topic.get("TOPIC 20") + 1);
									break;
								}
							}
							for(int j=0;j<TopicList21.size();j++){
								if(review.equals(TopicList21.get(j))){
									topic.put("TOPIC 21", topic.get("TOPIC 21") + 1);
									break;
								}
							}
							for(int j=0;j<TopicList22.size();j++){
								if(review.equals(TopicList22.get(j))){
									topic.put("TOPIC 22", topic.get("TOPIC 22") + 1);
									break;
								}
							}
							for(int j=0;j<TopicList23.size();j++){
								if(review.equals(TopicList23.get(j))){
									topic.put("TOPIC 23", topic.get("TOPIC 23") + 1);
									break;
								}
							}for(int j=0;j<TopicList24.size();j++){
								if(review.equals(TopicList24.get(j))){
									topic.put("TOPIC 24", topic.get("TOPIC 24") + 1);
									break;
								}
							}for(int j=0;j<TopicList25.size();j++){
								if(review.equals(TopicList25.get(j))){
									topic.put("TOPIC 25", topic.get("TOPIC 25") + 1);
									break;
								}
							}for(int j=0;j<TopicList26.size();j++){
								if(review.equals(TopicList26.get(j))){
									topic.put("TOPIC 26", topic.get("TOPIC 26") + 1);
									break;
								}
							}*/
				}
				Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {   
				    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
				        return (o2.getValue() - o1.getValue()); 
				    }
				});
				
				
				
				if(list.get(0).getValue().toString().equals(zero)){
					 writer.write("NO TOPIC MATCHES" + "\r\n");
				}
				else{
				  //System.out.println(list.get(0).getKey().toString());
					List<String> topicBag = new ArrayList<String>();
					
				  if(Integer.parseInt(list.get(0).getValue().toString())> Integer.parseInt(list.get(1).getValue().toString()))
				    writer.write(list.get(0).getKey().toString() + "\r\n");
				  
				  else{
				 	for(int i=0; i< list.size(); i++){
				 		if(Integer.parseInt(list.get(i).getValue().toString())==(Integer.parseInt(list.get(0).getValue().toString()))){
				 			topicBag.add(list.get(i).getKey()); // 将具有等量top的topics存入一个arraylist
				 			//System.out.println(list.get(i));
				 		}
				 		else
				 			break;
				 	}
				 	
				 	//随机选取一个topic
				 	Random rd = new Random();
				 	int i = rd.nextInt(topicBag.size());
				 	
				 	/*for(int f=0; f<topicBag.size(); f++){
				 		System.out.println("topicBag: " +topicBag.get(f));
				 	}
				 	System.out.println("selected topic: " +topicBag.get(i)); */
				 	
				 	writer.write(topicBag.get(i).toString() + "\r\n");
				 	topicBag.clear();
				  }
				}
				
				//clear ArrayList
				topic.clear();
				list.clear();
				
			}
			
			writer.flush(); 
	        writer.close();
	    
	        
			
			if (bigramFile != null)
				bigramFile.close();
			}
	  catch (IOException e) {
		System.err.println("Couldn't load folder: " + Config.optimalTag_LDA );
	  }
	}
}
