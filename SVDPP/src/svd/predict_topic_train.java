package svd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import config.Config;
import dataformat.UserItemRateTopicUnit;
import dataformat.UserItemRateTopicVector;
import dataformat.UserItemRateUnit;
import util.VectorCalc;

/**
 * @author Weinan Zhang 10 Dec 2012
 */
public class predict_topic_train extends SVD {

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

	public double[][][] P;
	public double[][][] Q;
	public double[] b_g;
	public double[][] b_u;
	public double[][] b_i;
	int k_num=5;
	
	public LinkedList<UserItemRateTopicVector> trainData;
	public LinkedList<UserItemRateTopicVector> testData;
	
	public static void main(String[] args) throws IOException {

		predict_topic_train a = new predict_topic_train();
		
		//System.out.println("Starting..");
		
		a.readInTrainAndTestData();
		//System.out.println("Loading data done.");
		
		a.initialize();
		//System.out.println("Initializing parameters done.");
		
		double rmse = a.train();
		System.out.println("Optimal accuracy: " + rmse);

	}
	
	double train(){
		double rmse = 0;
		double maxrmse = 0;
		System.out.println("Random data Accuracy: "+test());
		for(int i = 0; i < Config.roundNum; ++i){
			trainOneRound(); 
			if(Config.showResultsEachRound){
				rmse = test();
				maxrmse = Math.max(maxrmse, rmse);
				System.out.println("Round " + i + "\tAccuracy: " + rmse);
			}
		}
		rmse = test();
		maxrmse = Math.max(maxrmse, rmse);
		return maxrmse;
	}
	
	double test(){
		double sum=0;
		double count=0;
		double rmse = 0;
		double Precision=0;
		double Recall=0;
		double F1=0;
		int predicted_topic_num=0;
		int count_data=0;
		double[] TP=new double[Config.topicNum];
		double[] FP=new double[Config.topicNum];
		double[] TN=new double[Config.topicNum];
		double[] FN=new double[Config.topicNum];
		double[] Positive=new double[Config.topicNum];
		double[] True=new double[Config.topicNum];

		
		ArrayList<Double> predicted_topic = new ArrayList<Double>();
		for(int t=0; t<Config.topicNum; ++t)
			predicted_topic.add(0,0.0);
		
		for(UserItemRateTopicVector unit : testData){
			count_data=count_data+1;
			double[] hat_r_ui= new double[Config.topicNum];
			for(int tn=0;tn<Config.topicNum;++tn){
				hat_r_ui[tn] = b_g[tn] + b_u[unit.uid][tn] + b_i[unit.iid][tn] + VectorCalc.vectorInnerProduct(P[unit.uid][tn], Q[unit.iid][tn]);
				predicted_topic.set(tn, hat_r_ui[tn]);
			}
		predicted_topic_num=predicted_topic.indexOf(Collections.max(predicted_topic));
		if(predicted_topic_num==unit.topic_num){
			sum=sum+1.0;
			TP[unit.topic_num]=TP[unit.topic_num]+1;
		}else{
			FP[predicted_topic_num]=FP[predicted_topic_num]+1;
			FN[unit.topic_num]=FN[unit.topic_num]+1;
		}
		count=count+1.0;
		Positive[unit.topic_num]=Positive[unit.topic_num]+1;
		True[predicted_topic_num]=True[predicted_topic_num]+1;
		}

		
		//for(int i=0;i<Config.topicNum;++i){
		//	TN[i]=count_data+1-Positive[i]-FN[i];

		//}
		int count_prec=0;
		for(int i=0;i<Config.topicNum;++i){
			Recall=Recall+TP[i]/Positive[i];
			if(True[i]!=0){
				Precision=Precision+TP[i]/True[i];
				count_prec++;
			}
		}
		Precision=Precision/Config.topicNum;
		Recall=Recall/Config.topicNum;
		F1 = 2 * Precision * Recall / (Precision + Recall);
		
		System.out.println("Precision " + Precision + "\tRecall: " + Recall+ "\tF1: " + F1);
		rmse = Math.sqrt(rmse / testData.size());
		return sum/count;
		//return rmse;
		
		
		
	}
	
	void trainOneRound(){
		for(UserItemRateTopicVector unit : trainData){
			//Rating calculation: \hat{r}_ui = b_g + b_u + b_i + p_u^T q_i
			double[] hat_r_ui= new double[Config.topicNum];
			double[] err= new double[Config.topicNum];
			for(int tn=0;tn<Config.topicNum;++tn){
				hat_r_ui[tn] = b_g[tn] + b_u[unit.uid][tn] + b_i[unit.iid][tn] + VectorCalc.vectorInnerProduct(P[unit.uid][tn], Q[unit.iid][tn]);
				err[tn] = unit.topic[tn]- hat_r_ui[tn];
			}
			
			/** Parameters updating: 
				 * p_u = p_u + \eta ( err q_i - \lambda p_u) 
				 * q_i = q_i + \eta ( err p_u - \lambda q_i) 
				 * b_g = b_g + \eta err 
				 * b_u = b_u + \eta err
				 * b_i = b_i + \eta err 
				 * where err = r_ui - \hat{r}_ui
				 */
			for(int k = 0; k < k_num; ++k){
				for(int tn=0;tn<Config.topicNum;++tn){
					P[unit.uid][tn][k] = P[unit.uid][tn][k] + Config.eta * (err[tn] * Q[unit.iid][tn][k] - Config.lambda * P[unit.uid][tn][k]);
					Q[unit.iid][tn][k] = Q[unit.iid][tn][k] + Config.eta * (err[tn] * P[unit.uid][tn][k] - Config.lambda * Q[unit.iid][tn][k]);
				}
			}
			for(int tn=0;tn<Config.topicNum;++tn){
				b_g[tn] = b_g[tn] + Config.eta * err[tn];
				b_u[unit.uid][tn] = b_u[unit.uid][tn] + Config.eta * err[tn];
				b_i[unit.iid][tn] = b_i[unit.iid][tn] + Config.eta * err[tn]; 
			}
		}
	}
	
	void readInTrainAndTestData() throws IOException{
		BufferedReader reader;
		String[] splits;
		reader = new BufferedReader(new FileReader(Config.trainFile));
		trainData = new LinkedList<UserItemRateTopicVector>();
		testData = new LinkedList<UserItemRateTopicVector>();
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			trainData.add(new UserItemRateTopicVector(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), VectorCalc.num2vec(Integer.parseInt(splits[3])), Integer.parseInt(splits[3])));
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(Config.testFile));
		while(reader.ready()){
			splits = reader.readLine().split("\t");
			testData.add(new UserItemRateTopicVector(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Float.parseFloat(splits[2]), VectorCalc.num2vec(Integer.parseInt(splits[3])), Integer.parseInt(splits[3])));
		}
		reader.close();
	}
	
	void initialize() {
		Random rand = new Random();
		
		P = new double[Config.userNum][Config.topicNum][k_num];
		for(int u = 0; u < Config.userNum; ++u){
			P[u] = new double[Config.topicNum][k_num];
			for(int k = 0; k < k_num; ++k)
				for(int tn=0;tn<Config.topicNum;++tn){
					P[u][tn][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
				}
		}
		
		Q = new double[Config.itemNum][Config.topicNum][k_num];
		for(int i = 0; i < Config.itemNum; ++i){
			Q[i] = new double[Config.topicNum][k_num];
			for(int k = 0; k < k_num; ++k)
				for(int tn=0;tn<Config.topicNum;++tn){
					Q[i][tn][k] = (rand.nextDouble() - 0.5) * 2.0 * Config.initDelta; // [-initDelta, initDelta]
				}
		}
		
		b_g = new double[Config.topicNum];
		b_u = new double[Config.userNum][Config.topicNum];
		b_i = new double[Config.itemNum][Config.topicNum]; // all zero
	}
	
	void ROCtest(){
		
		
	}
}
