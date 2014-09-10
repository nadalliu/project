package svd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import config.Config;

import dataformat.UserItemRateTopicUnit;

import util.VectorCalc;

/**
 * @author Weinan Zhang 10 Dec 2012
 */
public class SVDRMSE extends SVD {

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
	
	public double[][] Yi;
	public double[][] Yu;
	public double b_g;
	public double[] b_u;
	public double[] b_i;
	
	public LinkedList<UserItemRateTopicUnit> trainData;
	public LinkedList<UserItemRateTopicUnit> testData;
	
	public void driver() throws IOException{
		System.out.println("Starting..");
		
		this.readInTrainAndTestData();
		System.out.println("Loading data done.");
		
		this.initialize();
		System.out.println("Initializing parameters done.");
		
		double rmse = this.train();
		System.out.println("Optimal RMSE: " + rmse);
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


			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(VectorCalc.sumvector(P[unit.uid] , Yu[unit.uid]), VectorCalc.sumvector(Q[unit.iid], Yi[unit.iid])); // java.lang.ArrayIndexOutOfBoundsException: 11377
			
			double squareErr = (unit.rate - hat_r_ui) * (unit.rate - hat_r_ui);

			rmse += squareErr;
		}
		rmse = Math.sqrt(rmse / testData.size());
		return rmse;
	}
	
	void trainOneRound(){
		
	
		for(UserItemRateTopicUnit unit : trainData){
			
			
				
			
			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(VectorCalc.sumvector(P[unit.uid] , Yu[unit.uid]), VectorCalc.sumvector(Q[unit.iid], Yi[unit.iid])); //P[unit.uid] and Q[unit.iid] are vectors
			//System.out.println(P[unit.uid][2]);
			double err = unit.rate - hat_r_ui;

			/** 
			 	* Rating calculation: \hat{r}_ui = b_g + b_u + b_i + p_u^T q_i
			    * Loss: (r_ui - \hat{r}_ui)^2 + \lambda(||q_i||^2 + ||p_u||^2)
			 * Parameters updating: 
				 * p_u = p_u + \eta ( err q_i - \lambda p_u) 
				 * q_i = q_i + \eta ( err p_u - \lambda q_i) 
				 * b_g = b_g + \eta err 
				 * b_u = b_u + \eta err
				 * b_i = b_i + \eta err 
				 * where err = r_ui - \hat{r}_ui
				 */
			for(int k = 0; k < Config.K; ++k){
				
				P[unit.uid][k] 	= P[unit.uid][k] 	+ Config.eta * (err * (Q[unit.iid][k]+Yi[unit.iid][k]) - Config.lambda * P[unit.uid][k] ); //P[unit.uid][k] and Q[unit.iid][k] are numbers
				Q[unit.iid][k] 	= Q[unit.iid][k] 	+ Config.eta * (err * (P[unit.uid][k]+Yu[unit.uid][k]) - Config.lambda * Q[unit.iid][k] );
				Yu[unit.uid][k] = Yu[unit.uid][k] 	+ Config.eta * (err * (Q[unit.iid][k]+Yi[unit.iid][k]) - Config.lambda * Yu[unit.uid][k]); 
				Yi[unit.iid][k] = Q[unit.iid][k] 	+ Config.eta * (err * (P[unit.uid][k]+Yu[unit.uid][k]) - Config.lambda * Yi[unit.iid][k]);
			}
			b_g = b_g + Config.eta * err;
			b_u[unit.uid] = b_u[unit.uid] + Config.eta * err;
			b_i[unit.iid] = b_i[unit.iid] + Config.eta * err;  
		}
		//System.exit(0);
	}
	
	void readInTrainAndTestData() throws IOException{
		BufferedReader reader;
		String[] splits;
		reader = new BufferedReader(new FileReader(Config.trainFile));
		trainData = new LinkedList<UserItemRateTopicUnit>();
		testData = new LinkedList<UserItemRateTopicUnit>();
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			trainData.add(new UserItemRateTopicUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), Integer.parseInt(splits[3])));
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
		double[] count_u;
		double[] count_i;
		P = new double[Config.userNum][Config.K];
		Yu = new double[Config.userNum][Config.K];
		count_u = new double[Config.K];
		count_i = new double[Config.K];
		for(int u = 0; u < Config.userNum; ++u){
			P[u] = new double[Config.K];// each user have K factors
			Yu[u] = new double[Config.K];
			// Topic Matrix for user
			for(UserItemRateTopicUnit unit : trainData){
				if(unit.uid==u){  //find the user-topic vector
					count_u[unit.topic]=count_u[unit.topic]+1;
					Yu[u][unit.topic]=Yu[u][unit.topic] + unit.rate*0.01;
				}
			}
			

			

			for(int k = 0; k < Config.K; ++k){
				P[u][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
				if(count_u[k]==0){
					count_u[k]=1;
				}
				Yu[u][k]=Yu[u][k]/count_u[k];
				//System.out.println(P[u][k]);
			}
			
		}
		
		Q = new double[Config.itemNum][Config.K];
		Yi = new double[Config.itemNum][Config.K];
		for(int i = 0; i < Config.itemNum; ++i){
			Q[i] = new double[Config.K];
			Yi[i] = new double[Config.K];
			// Topic Matrix for item
			for(UserItemRateTopicUnit unit : trainData){
				if(unit.iid==i){
					count_i[unit.topic]=count_i[unit.topic]+1;
					Yi[i][unit.topic]=Yu[i][unit.topic]+unit.rate*0.01;
					//System.out.println(Q[i][unit.topic]);
				}
			}

			for(int k = 0; k < Config.K; ++k){
				Q[i][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
				if(count_i[k]==0){
					count_i[k]=1;
				}
				Yi[i][k]=Yi[i][k]/count_i[k];
				//System.out.println(Q[i][k]);
			}
		}
		
		b_g = 0;
		b_u = new double[Config.userNum];
		b_i = new double[Config.itemNum]; // all zero
	}

}
