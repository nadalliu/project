package lzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import config.Config;

/**
 * @author Weinan Zhang
 * 15 Apr 2013
 */
public class Convert {

	/**
	 * @param args
	 */
	//static String input = "data/crunch-base-raw.txt";
	static String input = Config.uirTopic;//"N:/Project/ReviewTextsWithStemming/u_i_r_t.txt"
	static String outputUserIndex = Config.outputUserIndex;
	static String outputItemIndex = Config.outputItemIndex;
	static String outputUIIndexRT = Config.outputUIIndexRT;
	
	static Hashtable<String, Integer> userNameId = new Hashtable<String, Integer>();
	static Hashtable<String, Integer> itemNameId = new Hashtable<String, Integer>();
	
	//public static void main(String[] args) throws IOException {
	public void driver() throws IOException{		

		BufferedReader reader = new BufferedReader(new FileReader(input));
		BufferedWriter writerUser = new BufferedWriter(new FileWriter(outputUserIndex));
		BufferedWriter writerItem = new BufferedWriter(new FileWriter(outputItemIndex));
		BufferedWriter writerAll = new BufferedWriter(new FileWriter(outputUIIndexRT));
		
		int count = 0;
		while(reader.ready()){
			String line = reader.readLine();
			String[] splits = line.split(" ");
			String userName = splits[0];
			String itemName = splits[1];
			String rating = splits[2];
			String topic = splits[3] + " " + splits[4];
			if(!userNameId.containsKey(userName))
				userNameId.put(userName, userNameId.size());
			if(!itemNameId.containsKey(itemName))
				itemNameId.put(itemName, itemNameId.size());
			
				writerAll.write(userNameId.get(userName) + "\t" + itemNameId.get(itemName) + "\t" + rating + "\t" + topic + "\n");
				//writerAll.newLine();
			
		}
		for(String name : userNameId.keySet())
			writerUser.write(name + "\t" + userNameId.get(name) + "\n");
		for(String name : itemNameId.keySet())
			writerItem.write(name + "\t" + itemNameId.get(name) + "\n");
		
		
		
		writerAll.close();
		writerUser.close();
		writerItem.close();
	}

}
