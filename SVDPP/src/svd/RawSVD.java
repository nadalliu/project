package svd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import config.Config;

import dataformat.UserItemRateUnit;

import util.VectorCalc;

/**
 * @author Weinan Zhang 10 Dec 2012
 */
public class RawSVD extends SVD {

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
	public double b_g;
	public double[] b_u;
	public double[] b_i;
	
	public LinkedList<UserItemRateUnit> trainData;
	public LinkedList<UserItemRateUnit> testData;
	
	public double driver() throws IOException{
		//System.out.println("Starting..");
		
		this.readInTrainAndTestData();
		//System.out.println("Loading data done.");
		
		this.initialize();
		//System.out.println("Initializing parameters done.");
		
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
		for(UserItemRateUnit unit : testData){
			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(P[unit.uid], Q[unit.iid]);
			double squareErr = (unit.rate - hat_r_ui) * (unit.rate - hat_r_ui);
			rmse += squareErr;
		}
		rmse = Math.sqrt(rmse / testData.size());
		return rmse;
	}
	
	void trainOneRound(){
		for(UserItemRateUnit unit : trainData){
			//Rating calculation: \hat{r}_ui = b_g + b_u + b_i + p_u^T q_i
			double hat_r_ui = b_g + b_u[unit.uid] + b_i[unit.iid] + VectorCalc.vectorInnerProduct(P[unit.uid], Q[unit.iid]);
			double err = unit.rate - hat_r_ui;
			
			/** Parameters updating: 
				 * p_u = p_u + \eta ( err q_i - \lambda p_u) 
				 * q_i = q_i + \eta ( err p_u - \lambda q_i) 
				 * b_g = b_g + \eta err 
				 * b_u = b_u + \eta err
				 * b_i = b_i + \eta err 
				 * where err = r_ui - \hat{r}_ui
				 */
			for(int k = 0; k < Config.K; ++k){
				P[unit.uid][k] = P[unit.uid][k] + Config.eta * (err * Q[unit.iid][k] - Config.lambda * P[unit.uid][k]);
				Q[unit.iid][k] = Q[unit.iid][k] + Config.eta * (err * P[unit.uid][k] - Config.lambda * Q[unit.iid][k]);
			}
			b_g = b_g + Config.eta * err;
			b_u[unit.uid] = b_u[unit.uid] + Config.eta * err;
			b_i[unit.iid] = b_i[unit.iid] + Config.eta * err;  
		}
	}
	
	void readInTrainAndTestData() throws IOException{
		BufferedReader reader;
		String[] splits;
		reader = new BufferedReader(new FileReader(Config.trainFile));
		trainData = new LinkedList<UserItemRateUnit>();
		testData = new LinkedList<UserItemRateUnit>();
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			trainData.add(new UserItemRateUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2])));
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(Config.testFile));
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			testData.add(new UserItemRateUnit(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2])));
		}
		reader.close();
	}
	
	void initialize() {
		Random rand = new Random();
		
		P = new double[Config.userNum][Config.K];
		for(int u = 0; u < Config.userNum; ++u){
			P[u] = new double[Config.K];
			for(int k = 0; k < Config.K; ++k)
				P[u][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
		}
		
		Q = new double[Config.itemNum][Config.K];
		for(int i = 0; i < Config.itemNum; ++i){
			Q[i] = new double[Config.K];
			for(int k = 0; k < Config.K; ++k)
				Q[i][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
		}
		
		b_g = 0;
		b_u = new double[Config.userNum];
		b_i = new double[Config.itemNum]; // all zero
	}

}
