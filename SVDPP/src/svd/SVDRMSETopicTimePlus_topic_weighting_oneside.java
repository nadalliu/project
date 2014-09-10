package svd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
public class SVDRMSETopicTimePlus_topic_weighting_oneside extends SVD {

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
	
	public Hashtable<Integer, Hashtable<Integer,Integer>> userTopics;
	public Hashtable<Integer, Hashtable<Integer,Integer>> itemTopics;
	public Hashtable<Integer, Hashtable<Integer,Integer>> recent_userTopics;
	public Hashtable<Integer, Hashtable<Integer,Integer>> recent_itemTopics;
	
	public LinkedList<UserItemRateTopicUnit> trainData;
	public LinkedList<UserItemRateTopicUnit> recent_trainData;
	public LinkedList<UserItemRateTopicUnit> testData;
	
	public double driver() throws IOException{
		System.out.println("Starting..");
		
		this.readInTrainAndTestData();
		System.out.println("Loading data done.");
		
		this.initialize();
		System.out.println("Initializing parameters done.");
		
		double rmse = this.train();
		System.out.println("Optimal RMSE: " + rmse);
		return rmse;
	}
	
	double train(){
		double rmse = 0;
		double minrmse = Double.MAX_VALUE;
		
		for(int i = 0; i < Config.roundNum; ++i){
			trainOneRound(); 
			if(Config.showResultsEachRound){
				rmse = test();
				minrmse = Math.min(minrmse, rmse);
				System.out.println("Round " + i + "\tRMSE: " + rmse);
			}
		}
		rmse = test();
		minrmse = Math.min(minrmse, rmse);
		return minrmse;
	}
	
	double test(){
		double rmse = 0;
		
		for(UserItemRateTopicUnit unit : testData){  // UserItemRateUnit   int u, int i, float r = uid, iid, rate
			//System.out.println(unit.uid);
			
			/**
			 * Rating calculation: \hat{r}_ui = b_g + b_u + b_i + (p_u + Yu)^T (q_i + Yi)
			 * 
			 * Loss: (r_ui - \hat{r}_ui)^2 + \lambda(||q_i||^2 + ||p_u||^2 )
			 * 
			 */


			//double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(VectorCalc.sumvector(P[unit.uid] , Yu[unit.uid]), VectorCalc.sumvector(Q[unit.iid], Yi[unit.iid])); 
			
//			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(P[unit.uid], Q[unit.iid]);

			int uid = unit.uid;
			int iid = unit.iid;
			
			Hashtable<Integer,Integer> nu = userTopics.get(uid); //|N(u)|
			Hashtable<Integer,Integer> recent_nu = recent_userTopics.get(uid); //|N(u)|
			// |N(u)|^(-1/2) : nu.size()^(-1/2)
//			System.out.println("|N(u)|" + nu);
			
			
			Hashtable<Integer,Integer> ni = itemTopics.get(iid); //|N(i)|
			Hashtable<Integer,Integer> recent_ni = recent_itemTopics.get(iid); //|N(i)|
//			userTopics.put(iid, ni);
			
			double[] ut = new double[Config.K];
			double[] it = new double[Config.K];
			
			double[] recent_ut = new double[Config.K];
			double[] recent_it = new double[Config.K];
			
			int sum_count=0;
			int sum_key=0;
			
/////////////////////////////////////////////
			for(int k = 0; k < Config.K; ++k){
				ut[k] = 0;
				recent_ut[k] = 0;
			}
			if(nu.size() > 0){
				for(Integer t : nu.keySet()){
					sum_count=sum_count+nu.get(t);
					sum_key=sum_key+1;
					ut = VectorCalc.sumvector(ut, Y[t]);
				}
				ut = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(ut, sum_count/sum_key), Math.sqrt(nu.size()));
			}
			
/////////////////////////////////////////////
			sum_count=0;
			sum_key=0;
			
			if(recent_nu.size() > 0){
				for(Integer t : recent_nu.keySet()){
					sum_count=sum_count+recent_nu.get(t);
					sum_key=sum_key+1;
					recent_ut = VectorCalc.sumvector(recent_ut, T[t]);
				}
				recent_ut = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(recent_ut, sum_count/sum_key), Math.sqrt(recent_nu.size()));
			}
			ut = VectorCalc.sumvector(ut, P[uid]);
			ut = VectorCalc.sumvector(ut, recent_ut);
			
			
/////////////////////////////////////////////
/*			sum_count=0;
			sum_key=0;
			
			
			for(int k = 0; k < Config.K; ++k){
				it[k] = 0;
				recent_it[k] = 0;
			}
			
			if (ni.size() > 0){
				for(Integer t : ni.keySet()){
					sum_count=sum_count+ni.get(t);
					sum_key=sum_key+1;
					it = VectorCalc.sumvector(it, Y[t]);
				}
				it = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(it, sum_count/sum_key), Math.sqrt(ni.size()));
			}
/////////////////////////////////////////////
			sum_count=0;
			sum_key=0;
			
			if(recent_ni.size() > 0){
				for(Integer t : recent_ni.keySet()){
					sum_count=sum_count+recent_ni.get(t);
					sum_key=sum_key+1;
					recent_it = VectorCalc.sumvector(recent_it, T[t]);
				}
				recent_it = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(recent_it, sum_count/sum_key), Math.sqrt(recent_ni.size()));
			}

			it = VectorCalc.sumvector(it, Q[iid]);
			it = VectorCalc.sumvector(it, recent_it);
*/			
/////////////////////////////////////////////
			//float hat_r_ui = 
			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid]
			                  + VectorCalc.vectorInnerProduct(ut, Q[iid]);
			
			
			Double squareErr = (unit.rate - hat_r_ui) * (unit.rate - hat_r_ui);
			

			rmse += squareErr;
		}
		rmse = Math.sqrt(rmse / testData.size());
		return rmse;
	}
	
	void trainOneRound(){
		
	
		for(UserItemRateTopicUnit unit : trainData){
			
			int uid = unit.uid;
			int iid = unit.iid;
			int tid = unit.topic;
			float rate = unit.rate;
			
			Hashtable<Integer,Integer> nu = userTopics.get(uid); //|N(u)|
			Hashtable<Integer,Integer> recent_nu = recent_userTopics.get(uid); //|N(u,t)|
			// |N(u)|^(-1/2) : nu.size()^(-1/2)
//			System.out.println("|N(u)|" + nu);
			
			
			Hashtable<Integer,Integer> ni = itemTopics.get(iid); //|N(i)|
			Hashtable<Integer,Integer> recent_ni = recent_itemTopics.get(iid); //|N(i,t)|
//			userTopics.put(iid, ni);
			
			double[] ut = new double[Config.K];
			double[] it = new double[Config.K];
			double[] recent_ut = new double[Config.K];
			double[] recent_it = new double[Config.K];
			
			int sum_count=0;
			int sum_key=0;
			
/////////////////////////////////////////////
			for(int k = 0; k < Config.K; ++k){
				ut[k] = 0;
				recent_ut[k] = 0;
			}
			if(nu.size() > 0){
				for(Integer t : nu.keySet()){
					sum_count=sum_count+nu.get(t);
					sum_key=sum_key+1;
					ut = VectorCalc.sumvector(ut, Y[t]);
				}
				ut = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(ut, sum_count/sum_key), Math.sqrt(nu.size()));
			}
			
/////////////////////////////////////////////
			sum_count=0;
			sum_key=0;
			
			if(recent_nu.size() > 0){
				for(Integer t : recent_nu.keySet()){
					sum_count=sum_count+recent_nu.get(t);
					sum_key=sum_key+1;
					recent_ut = VectorCalc.sumvector(recent_ut, T[t]);
				}
				recent_ut = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(recent_ut, sum_count/sum_key), Math.sqrt(recent_nu.size()));
			}
			ut = VectorCalc.sumvector(ut, P[uid]);
			ut = VectorCalc.sumvector(ut, recent_ut);
			
			
/////////////////////////////////////////////
/*			sum_count=0;
			sum_key=0;
			
			
			for(int k = 0; k < Config.K; ++k){
				it[k] = 0;
				recent_it[k] = 0;
			}
			
			if (ni.size() > 0){
				for(Integer t : ni.keySet()){
					sum_count=sum_count+ni.get(t);
					sum_key=sum_key+1;
					it = VectorCalc.sumvector(it, Y[t]);
				}
				it = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(it, sum_count/sum_key), Math.sqrt(ni.size()));
			}
/////////////////////////////////////////////
			sum_count=0;
			sum_key=0;
			
			if(recent_ni.size() > 0){
				for(Integer t : recent_ni.keySet()){
					sum_count=sum_count+recent_ni.get(t);
					sum_key=sum_key+1;
					recent_it = VectorCalc.sumvector(recent_it, T[t]);
				}
				recent_it = VectorCalc.vectorDivideNum(VectorCalc.vectorDivideNum(recent_it, sum_count/sum_key), Math.sqrt(recent_ni.size()));
			}

			it = VectorCalc.sumvector(it, Q[iid]);
			it = VectorCalc.sumvector(it, recent_it);
	*/		
/////////////////////////////////////////////
			
		
			
			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid]
	                  + VectorCalc.vectorInnerProduct(ut, Q[iid]);
			//double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(VectorCalc.sumvector(P[unit.uid] , Yu[unit.uid]), VectorCalc.sumvector(Q[unit.iid], Yi[unit.iid])); //P[unit.uid] and Q[unit.iid] are vectors
			
			double err = unit.rate - hat_r_ui;
			
			/** 
			 	* Rating calculation: \hat{r}_ui = b_g + b_u + b_i + ut^T it
			    * Loss: (r_ui - \hat{r}_ui)^2 + \lambda(||q_i||^2 + ||p_u||^2 + sum||y_j||^2)
			 * Parameters updating: 
				 * p_u = p_u + \eta ( err it - \lambda p_u) 
				 * q_i = q_i + \eta ( err ut - \lambda q_i) 
				 * y_j = y_j + \eta ( err it - \lambda y_j) * |nu|^(-1/2)
				 * b_g = b_g + \eta err 
				 * b_u = b_u + \eta err
				 * b_i = b_i + \eta err 
				 * where err = r_ui - \hat{r}_ui
				 */
			for(int k = 0; k < Config.K; ++k){
				
				P[uid][k] = P[uid][k] * (1 - Config.lambda * Config.eta)+ Config.eta * err * Q[iid][k];
				Q[iid][k] = Q[iid][k] * (1 - Config.lambda * Config.eta) + Config.eta * err * ut[k];
				for (Integer t : nu.keySet())
					Y[t][k] = Y[t][k] * (1 - Config.lambda1 * Config.eta / Math.sqrt(nu.size()))
							+ Config.eta * err * Q[iid][k]/Math.sqrt(nu.size());
//				for (Integer t : ni.keySet())
//					Y[t][k] = Y[t][k] * (1 - Config.lambda * Config.eta / Math.sqrt(ni.size()))
//							+ Config.eta * err * ut[k];
				for (Integer t : recent_nu.keySet())
					T[t][k] = T[t][k] * (1 - Config.lambda1 * Config.eta / Math.sqrt(recent_nu.size()))
							+ Config.eta * err * Q[iid][k]/Math.sqrt(recent_nu.size());
//				for (Integer t : recent_ni.keySet())
//					T[t][k] = T[t][k] * (1 - Config.lambda * Config.eta / Math.sqrt(recent_ni.size()))
//							+ Config.eta * err * ut[k];
			}
			b_g = b_g + Config.eta * err;
			b_u[unit.uid] = b_u[unit.uid] + Config.eta * err;
			b_i[unit.iid] = b_i[unit.iid] + Config.eta * err;  
		}
		
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
	
	void initialize() {
		Random rand = new Random();
		userTopics = new Hashtable<Integer, Hashtable<Integer,Integer>>();
		itemTopics = new Hashtable<Integer, Hashtable<Integer,Integer>>();
		recent_userTopics = new Hashtable<Integer, Hashtable<Integer,Integer>>();
		recent_itemTopics = new Hashtable<Integer, Hashtable<Integer,Integer>>();
		Hashtable<Integer, Integer> topic_num = new Hashtable<Integer, Integer>();

		P = new double[Config.userNum][Config.K];
		
		for(int u = 0; u < Config.userNum; ++u){
			userTopics.put(u, new Hashtable<Integer,Integer>());
			recent_userTopics.put(u, new Hashtable<Integer,Integer>());
			P[u] = new double[Config.K];// each user have K factors
			
			for(int k = 0; k < Config.K; ++k){
				P[u][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
			}
		}
		
		Q = new double[Config.itemNum][Config.K];
		for(int i = 0; i < Config.itemNum; ++i){
			itemTopics.put(i, new Hashtable<Integer,Integer>());
			recent_itemTopics.put(i, new Hashtable<Integer,Integer>());
			Q[i] = new double[Config.K];

			for(int k = 0; k < Config.K; ++k){
				Q[i][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
			}
		}
		
		b_g = 0;
		b_u = new double[Config.userNum];
		b_i = new double[Config.itemNum]; // all zero
		
		// get nu and ni
		for(UserItemRateTopicUnit unit : trainData){
			int uid = unit.uid;
			int iid = unit.iid;
			int tid = unit.topic;
			
			if(!userTopics.get(uid).containsKey(tid))
				userTopics.get(uid).put(tid, 1);
			userTopics.get(uid).put(tid, userTopics.get(uid).get(tid)+1);
//			if(!itemTopics.get(iid).containsKey(tid))
//				itemTopics.get(iid).put(tid, 1);
//			itemTopics.get(iid).put(tid, itemTopics.get(iid).get(tid)+1);
		}
		
		for(UserItemRateTopicUnit unit : recent_trainData){
			int uid = unit.uid;
			int iid = unit.iid;
			int tid = unit.topic;

			if(!recent_userTopics.get(uid).containsKey(tid))
				recent_userTopics.get(uid).put(tid, 1);
			recent_userTopics.get(uid).put(tid, recent_userTopics.get(uid).get(tid)+1);
//			if(!recent_itemTopics.get(iid).containsKey(tid))
//				recent_itemTopics.get(iid).put(tid, 1);
//			recent_itemTopics.get(iid).put(tid, recent_itemTopics.get(iid).get(tid)+1);
		}
		
		Y = new double[Config.topicNum][];
		for(int j = 0; j < Config.topicNum; ++j){
			Y[j] = new double[Config.K];// each user have K factors
			for(int k = 0; k < Config.K; ++k){
				Y[j][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
			}
		}
		T = new double[Config.topicNum][];
		for(int j = 0; j < Config.topicNum; ++j){
			T[j] = new double[Config.K];// each user have K factors
			for(int k = 0; k < Config.K; ++k){
				T[j][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
			}
		}
	}

}
