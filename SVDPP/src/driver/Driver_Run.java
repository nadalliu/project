package driver;

import java.io.IOException;
import java.util.ArrayList;

import config.Config;
import svd.RawSVD;
import svd.SVDRMSE;
import svd.SVDRMSETopicPlus;
import svd.SVDRMSETopicTimePlus;
import svd.SVDRMSETopicTimePlus_one_side;
import svd.SVDRMSETopicTimePlus_topic_weighting;
import svd.SVDRMSETopicTimePlus_topic_weighting_new;
import svd.SVDRMSETopicTimePlus_topic_weighting_oneside;
import test.SVDRMSETopicTimePlus2;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class Driver_Run {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		RawSVD svd = new RawSVD();
	    svd.driver();
		
		//SVDRMSETopicPlus svdrmseTopicPlus = new SVDRMSETopicPlus();
		//svdrmseTopicPlus.driver();
		
		//SVDRMSETopicTimePlus svdrmseTopicTimePlus = new SVDRMSETopicTimePlus();
		//svdrmseTopicTimePlus.driver();
		
	   // SVDRMSETopicTimePlus_topic_weighting svdrmseTopicTimePlus_topic_weighting = new SVDRMSETopicTimePlus_topic_weighting();
		//svdrmseTopicTimePlus_topic_weighting.driver();
		

	    SVDRMSETopicTimePlus_topic_weighting_new svdrmseTopicTimePlus_topic_weighting_new = new SVDRMSETopicTimePlus_topic_weighting_new();
	    svdrmseTopicTimePlus_topic_weighting_new.driver();
		
		
		//SVDRMSETopicTimePlus_topic_weighting_oneside svdrmseTopicTimePlus_oneside = new SVDRMSETopicTimePlus_topic_weighting_oneside();
		//svdrmseTopicTimePlus_oneside.driver();
		
		//SVDRMSETopicTimePlus_one_side svdrmseTopicTimePlus_oneside = new SVDRMSETopicTimePlus_one_side();
		//svdrmseTopicTimePlus_oneside.driver();
		
		
	}

}
