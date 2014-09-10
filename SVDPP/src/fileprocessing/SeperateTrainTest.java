package fileprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import config.Config;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class SeperateTrainTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IOException 
	 */
	
	public static void work(String input, String output1, String output2) throws IOException{
		
		String noTopic = "NO TOPIC";
		BufferedReader reader = new BufferedReader(new FileReader(input));
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(output1));
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(output2));
		
		int lineNum = 0;
		while(reader.ready()){
			String[] splits = reader.readLine().split("\t");
			
			if(splits[3].toString().equals(noTopic)){
				splits[3] = "TOPIC "+ Config.lastTopic;
			}
			String[] topics = splits[3].split(" "); // TOPIC NUMBER
			
			if(++lineNum % 5 != 0)
				writer1.write(Integer.parseInt(splits[0]) + "\t" + Integer.parseInt(splits[1]) + "\t" + splits[2] + "\t" + topics[1] +"\n");
			else
				writer2.write(Integer.parseInt(splits[0]) + "\t" + Integer.parseInt(splits[1])  + "\t" + splits[2] + "\t" + topics[1] + "\n");
		}
		reader.close();
		writer1.close();
		writer2.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String input = Config.rawFile;
		String output1 = Config.trainFile; // train data out path
		String output2 = Config.testFile;  // test data out path
		
		File file1 = new File(output1); 
        if (!file1.exists()) {  
            file1.createNewFile();  
        }  
        
        File file2 = new File(output2); 
        if (!file2.exists()) {  
            file2.createNewFile();  
        }  
		
		work(input, output1, output2);
		
		System.out.println("DONE.");
	}

}

