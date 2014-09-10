package driver;

import java.io.IOException;

import liuyang.nlp.lda.main.LdaGibbsSampling;
import lzy.Convert;
import lzy.DataPreprocessing;
import lzy.DataProcessing;
import lzy.Tag;
import lzy.firstDataProcessing;
import lzy.onlyReview;


public class Driver {
	

	public static void main(String[] args) throws IOException {
		
		
		
		/*
		 * separate user reviews into small files
		 * input: one file: user reviews. ***To get it, use EXCEL to extract user review only.***
		 * output: many files, each file contains one user review
		 */
		System.out.println("Seperating user reviews into small files ...");
		DataProcessing dataProcess = new DataProcessing();
		dataProcess.driver();
		System.out.println("done.");
		
		/*
		 * topic extraction using LDA ***don't forget to configure parameter in data/LdaParameter/LdaParameters.txt***
		 * input:seperate user review files
		 * output: one lda_topic file
		 */
		System.out.println("Topic Extraction using LDA ...");
		LdaGibbsSampling lda = new LdaGibbsSampling();
		lda.driver();
		System.out.println("done.");
		
		/*
		 * convert review into topic
		 * Attention: re-configure arraylist settings according to the number of topic IN ONLYREVIEW.JAVA
		 * input: one lda_topic file, stemmed user review
		 * output: optimal topic
		 */
		System.out.println("Converting review into topic ...");
		onlyReview onlyreview = new onlyReview();
		onlyreview.driver();
		System.out.println("done.");
		
		/*
		 * combine user id, item id, rating and topic
		 * input: original u-i-r(extracting from EXCEL), optimal topic
		 * output: u-i-r-t with encrypted user id and encryted item id
		 * Tip: In shoes Amazon dataset, user id is ahead of item-id, which is different from the output from DataPreProcessing. 
		 */
		System.out.println("Combining user_id, item_id, rating into topic ...");
		Tag tag = new Tag();
		tag.driver();
		System.out.println("done.");
		
		/*
		 * convert encrypted user id and encryted item id into a unqiue number counting from 0
		 * input: u-i-r-t with encrypted user id and encryted item id
		 * output: uid-iid-r-t 
		 */
		System.out.println("Converting encrypted user id and encryted item id into a unqiue number ...");
		Convert convert = new Convert();
		convert.driver();
		System.out.println("done.");
		
	}

}
