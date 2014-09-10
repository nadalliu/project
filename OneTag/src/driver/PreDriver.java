package driver;

import java.io.IOException;

import lzy.DataPreprocessing;
import lzy.firstDataProcessing;


public class PreDriver {
	

	public static void main(String[] args) throws IOException {
		
		/*
		 * Processing shoes Amazaon, No need to yelp.
		 * Input: raw file
		 * Output: The file with five parameters: item-id, user-id, rating, time, review (In this order)
		 * Attention: for further step, item-id and user-id must be exchanged their column in EXCEL. 
		 */
        System.out.println("File initialising...");
		DataPreprocessing dataPreProcess = new DataPreprocessing();
		dataPreProcess.driver();
		System.out.println("done.");
		
		/*
		 * This step is just used for processing yelp dataset. No need to shoes Amazon dataset
		 * Input: raw File
		 * Output: one user review file
		 */
	
		/*firstDataProcessing firstdataProcess = new firstDataProcessing();
		firstdataProcess.driver();
		System.out.println("done.");*/
	}
}
		