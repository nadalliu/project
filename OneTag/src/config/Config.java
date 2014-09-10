package config;


public class Config {
	
	static{
		
		   
			topicNum = 11; // except "no topic"
			Pre_inputFile = "N:/Project/New/shoes/shoes1.txt"; // for shoes
			Pre_outputFile = "N:/Project/New/shoes/Processed_shoes1.txt"; // for shoes
			stemmedInputFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/stemmed_Processed_shoes_review1.txt"; //initial Review   N:/Project/ReviewTextsWithStemming/stemmed_yelp_review1.txt
			stemmedOutputFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/MidoutTag.txt";//processed review    N:/Project/ReviewTextsWithStemming/MidoutTag.txt
			seperateReivewFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/ReviewTexts1/"; // seperate review file
			Tag_LDA = "N:/Project/New/Shoes/ReviewTextsWithStemming/Tag_LDA/"; // Tag file
		    optimalTag_LDA = "N:/Project/New/Shoes/ReviewTextsWithStemming/Tag_LDA/lda_3000.twords";
		    tagOutputFile = "N:/Project/New/Shoes/ReviewTextsWithStemming/outTag1.txt";
		    rawUIR = "N:/Project/New/Shoes/ReviewTextsWithStemming/u_i_r.txt";
		    uirTopic = "N:/Project/New/Shoes/ReviewTextsWithStemming/u_i_r_t.txt";
		    outputUserIndex = "N:/Project/New/Shoes/ReviewTextsWithStemming/uid-name.txt";
		    outputItemIndex = "N:/Project/New/Shoes/ReviewTextsWithStemming/iid-name.txt";
		    outputUIIndexRT = "N:/Project/New/Shoes/ReviewTextsWithStemming/uid-iid-r-t.txt";
	}
	
	
	
	public static int topicNum; // except "no topic"
	public static String Pre_inputFile;  
	public static String Pre_outputFile;  
	
	public static String stemmedInputFile; //initial Review  
	public static String stemmedOutputFile; // Processed Review
	public static String seperateReivewFile;
	public static String Tag_LDA;
	public static String optimalTag_LDA;
	public static String tagOutputFile;
	public static String rawUIR;
	public static String uirTopic;
	public static String outputUserIndex;
	public static String outputItemIndex;
	public static String outputUIIndexRT;
}
