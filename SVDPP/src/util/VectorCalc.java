package util;

import config.Config;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class VectorCalc {
	public static double vectorInnerProduct(double[] p, double[] q){
		double r = 0;
		for(int k = 0; k < p.length; ++k){ // NaN
			r += p[k] * q[k];
		} 

		return r;
	}
	
	
	public static double[] sumvector(double[] p, double[] q){
		double[] r;
		r = new double[p.length];
		for(int k = 0; k < p.length; ++k){ // NaN
			r[k]= p[k] + q[k];

			
		} 

		return r;
	}	
	
	
	
	public static double[] vectorElementProduct(double[] p, double[] q){
		double[] r;
		r = new double[p.length];
		for(int k = 0; k < p.length; ++k)
			r[k] = p[k] * q[k];
		return r;
	}
	
	public static double vectorMaxElement(double[] p){
		double max = Double.MIN_VALUE;
		for(int i = 0; i < p.length; i++)
		{
			if(p[i] > max)
				max = p[i];
		}
		return max;
	}
	
	public static double[] vectorDivideNum(double[] p, double n){
		double[] res = new double[p.length];
		
		for(int i = 0; i < p.length; i++)
		{
			res[i] = p[i] / n;

		}
		return res;
	}
	
	public static double[] num2vec(int p){
		double[] res = new double[Config.topicNum];
		
		for(int i = 0; i < Config.topicNum; i++){
			if(i==p)
				res[i] = 1;
			else
				res[i] = 0;
		}
		return res;
	}
	
	
	
}


