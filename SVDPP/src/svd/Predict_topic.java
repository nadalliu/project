package svd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

import util.VectorCalc;
import config.Config;
import dataformat.UserItemRateTopicUnit;

/**
 * @author Weinan Zhang 10 Dec 2012
 */
public class Predict_topic{

	/**
	 * Rating calculation: \hat{r}_ui = b_g + b_u + b_i + p_u^T q_i
	 * 
	 * Loss: (r_ui - \hat{r}_ui)^2 + \lambda(||q_i||^2 + ||p_u||^2)
	 * 
	 * Parameters updating: 
	 * p_u = p_u + \eta ( err q_i - \lambda p_u) 
	 * q_i = q_i + \eta ( err p_u - \lambda q_i) 
	 * b_g = b_g + \eta err 
	 * b_u = b_u + \eta err
	 * b_i = b_i + \eta err 
	 * where err = r_ui - \hat{r}_ui
	 */

	public double[][] P;
	public double[][] Q;
	public double[][] Y;
	public double[][] T;
	
	public double b_g;
	public double[] b_u;
	public double[] b_i;
	
	public Hashtable<Integer, Hashtable<Integer,Double>> userTopics;
	public Hashtable<Integer, Hashtable<Integer,Double>> itemTopics;
	public Hashtable<Integer, Hashtable<Integer,Double>> recent_userTopics;
	public Hashtable<Integer, Hashtable<Integer,Double>> recent_itemTopics;
	
	public LinkedList<UserItemRateTopicUnit> trainData;
	public LinkedList<UserItemRateTopicUnit> recent_trainData;
	public LinkedList<UserItemRateTopicUnit> testData;
	
	public static void main(String[] args) throws IOException {

		Predict_topic a = new Predict_topic();
		
		a.readInTrainAndTestData();
		
		a.initialize();
		
		a.test();
		
		
	}
	
	
	
	void readInTrainAndTestData() throws IOException{
		BufferedReader reader;
		String[] splits;
		trainData = new LinkedList<UserItemRateTopicUnit>();
		recent_trainData = new LinkedList<UserItemRateTopicUnit>();
		testData = new LinkedList<UserItemRateTopicUnit>();
		
		
		reader = new BufferedReader(new FileReader(Config.trainFile));
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			trainData.add(new UserItemRateTopicUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), Integer.parseInt(splits[3])));
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(Config.recent_trainFile));
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			recent_trainData.add(new UserItemRateTopicUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), Integer.parseInt(splits[3])));
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(Config.testFile));
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			testData.add(new UserItemRateTopicUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), Integer.parseInt(splits[3])));
		}
		reader.close();
	}
	
	
	void initialize() throws IOException{
		userTopics = new Hashtable<Integer, Hashtable<Integer,Double>>();
		itemTopics = new Hashtable<Integer, Hashtable<Integer,Double>>();
		
		for(int u = 0; u < Config.userNum; ++u){
			userTopics.put(u, new Hashtable<Integer,Double>());
		}
		for(int i = 0; i < Config.itemNum; ++i){
			itemTopics.put(i, new Hashtable<Integer,Double>());
		}
		
		

		for(UserItemRateTopicUnit unit : trainData){
			int uid = unit.uid;
			int iid = unit.iid;
			int tid = unit.topic;
			
			if(!userTopics.containsKey(uid))
				userTopics.put(uid, new Hashtable<Integer,Double>());
			if(!userTopics.get(uid).containsKey(tid))
				userTopics.get(uid).put(tid, 0.0);
			userTopics.get(uid).put(tid,userTopics.get(uid).get(tid)+1.0);
			
			if(!itemTopics.containsKey(iid))
				itemTopics.put(iid, new Hashtable<Integer,Double>());
			if(!itemTopics.get(iid).containsKey(tid))
				itemTopics.get(iid).put(tid, 0.0);
			itemTopics.get(iid).put(tid,itemTopics.get(iid).get(tid)+1.0);
		}
		
		for(int u = 0; u < Config.userNum; ++u){
			double review_sum=0;
			for(int t = 0; t < Config.topicNum; ++t){
				if(userTopics.get(u).containsKey(t))
					review_sum=review_sum + userTopics.get(u).get(t);
				else
					userTopics.get(u).put(t, 0.0);
			}
			
			if(review_sum!=0){
			for(int t = 0; t < Config.topicNum; ++t){
				if(userTopics.get(u).containsKey(t)){
					userTopics.get(u).put(t,userTopics.get(u).get(t)/review_sum);
 				}
			}
			}
		}
		
		for(int i = 0; i < Config.itemNum; ++i){
			double review_sum=0;
			for(int t = 0; t < Config.topicNum; ++t){
				if(itemTopics.get(i).containsKey(t))
					review_sum=review_sum + itemTopics.get(i).get(t);
				else
					itemTopics.get(i).put(t, 0.0);
			}
			if(review_sum!=0){
			for(int t = 0; t < Config.topicNum; ++t){
				if(itemTopics.get(i).containsKey(t))
					itemTopics.get(i).put(t,itemTopics.get(i).get(t)/review_sum);
			}
			}
			
		}
		
	}
	
	
	void test(){
		ArrayList<Integer> predicted_topic = new ArrayList<Integer>();
		double sum=0;
		double count=0;
		int num=1;
		for(int t=0; t<num; ++t)
			predicted_topic.add(0,0);

		for(UserItemRateTopicUnit unit : testData){  
			ArrayList<Double> sum_topic = new ArrayList<Double>();
			ArrayList<Double> sum_topic_sort = new ArrayList<Double>();
			int uid = unit.uid;
			int iid = unit.iid;
			int tid = unit.topic;
			
			for(int t = 0; t < Config.topicNum; ++t){
				sum_topic.add(t,userTopics.get(uid).get(t)+itemTopics.get(iid).get(t));
				sum_topic_sort.add(t,userTopics.get(uid).get(t)+itemTopics.get(iid).get(t));
			}
			
			Collections.sort(sum_topic_sort);
			
			for(int i=0; i<num; i++)
				predicted_topic.set(i,sum_topic.indexOf(sum_topic_sort.get(Config.topicNum-1-i)));
			if(predicted_topic.contains(tid))
				sum=sum+1.0;
			count=count+1.0;
			
		}
		
		System.out.println(sum/count);
		
	}
	
}