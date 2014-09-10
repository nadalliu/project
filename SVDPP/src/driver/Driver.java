package driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import config.Config;
import svd.RawSVD;
import svd.SVDRMSE;
import svd.SVDRMSETopicPlus;
import svd.SVDRMSETopicTimePlus;
import svd.SVDRMSETopicTimePlus_one_side;
import svd.SVDRMSETopicTimePlus_topic_weighting;
import svd.SVDRMSETopicTimePlus_topic_weighting_oneside;
import test.SVDRMSETopicTimePlus2;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class Driver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		int[] K = {25,30,35}; // random number   
		double[] eta = {0.0008}; //learning rate
		double[] lambda = {0.01, 0.05, 0.1}; //
		double[] lambda1 = {0.01, 0.05, 0.1};
		double[] initDelta = {0.01, 0.05, 0.1}; //init upper value for p and q
		
		
		
		ArrayList<Double> rmse = new ArrayList<Double>();
		//RawSVD rawsvd = new RawSVD();
		//rawsvd.driver();
		Config.showResultsEachRound=false;

		
		//SVDRMSETopicPlus svdrmseTopicPlus = new SVDRMSETopicPlus();
		//svdrmseTopicPlus.driver();
		
		//SVDRMSETopicTimePlus svdrmseTopicTimePlus = new SVDRMSETopicTimePlus();
		//svdrmseTopicTimePlus.driver();
		
		//SVDRMSETopicTimePlus_topic_weighting svdrmseTopicTimePlus_topic_weighting = new SVDRMSETopicTimePlus_topic_weighting();
		//svdrmseTopicTimePlus_topic_weighting.driver();
		
		
		//SVDRMSETopicTimePlus_topic_weighting_oneside svdrmseTopicTimePlus_oneside = new SVDRMSETopicTimePlus_topic_weighting_oneside();
		//svdrmseTopicTimePlus_oneside.driver();
		
		ArrayList<String> arrayOutput = new ArrayList<String>();
		
		for (int k : K){
			Config.K=k;
			for (double e: eta){
				Config.eta=e;
				for (double l: lambda){
					Config.lambda=l;
					for (double m: lambda1){
						Config.lambda1=m;
					for (double d: initDelta){
						Config.initDelta=d;
						
						SVDRMSETopicTimePlus_topic_weighting svdrmseTopicTimePlus_topic_weighting = new SVDRMSETopicTimePlus_topic_weighting();
						//svdrmseTopicTimePlus_topic_weighting.driver();
						double r = svdrmseTopicTimePlus_topic_weighting.driver();
						String out = "K = "+ k + " eta = " + e + " lambda = " + l + " lambda1 = " + m + " initDelta = " + d + " Optimal RMSE = " + r;
						System.out.println(out);
						arrayOutput.add(out);
					}
				}
			}
		 }
		}
		String output1 = "N:/Project/OptimalPara.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(output1));
		File file1 = new File(output1); 
        if (!file1.exists()) {  
            file1.createNewFile();  
        }  

		for(int i =0; i<arrayOutput.size();i++){
			writer.write(arrayOutput.get(i));
		}
		writer.close();
	}

}
