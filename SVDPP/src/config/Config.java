package config;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class Config {
	//public static String[] datasets = {"ml-100k", "ml-1m"};
	//public static String dataset = "ml-100k";
	
	static{
		//if(dataset.equals("ml-100k")){
		    userNum = 73590;
			itemNum = 48410; //
			topicNum = 11;
			lastTopic = 10;
			K = 3; // random number   
			eta = 0.005; //learning rate
			lambda = 0.01; //regularization parameter 0.01 0.05 0.008 for Pu Qi
			lambda1 = 0.1; //regularization parameter 0.01 0.05 0.008 for N()
			initDelta = 0.001; //init upper value for p and q
			rawFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/uid-iid-r-t.txt";
			trainFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/output/train.txt";
			recent_trainFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/output/recent_train.txt";
			testFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/output/test.txt";
			roundNum = 450;
			showResultsEachRound = true;
		
		
	}
	
	
	public static int userNum;
	public static int itemNum;
	public static int topicNum;
	public static int lastTopic;
	public static int K;
	
	public static double eta; //learning rate
	public static double lambda; //regularization parameter
	public static double lambda1; //regularization parameter
	public static double initDelta; //init upper value for p and q
	
	public static String rawFile;
	public static String trainFile;
	public static String recent_trainFile;
	public static String testFile;
	
	public static int roundNum;
	public static boolean showResultsEachRound;
	
	//public static int relevantThresholdScore = 4;
	//public static int topN = 1;
}
