package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class test {
	public static Hashtable<Integer, ArrayList<Integer>> userTopics;
	
	public static void main(String[] args) throws IOException {
		int sum;
		userTopics = new Hashtable<Integer, ArrayList<Integer>>();
		Hashtable<Integer, Integer> numbers = new Hashtable<Integer, Integer>();
		numbers.put(1, 1);
		userTopics.put(1, new ArrayList<Integer>());
		userTopics.put(2, new ArrayList<Integer>());
		userTopics.get(1).add(2);
		userTopics.get(1).add(3);
		userTopics.get(1).add(2);
		userTopics.get(1).add(4);
		userTopics.get(2).add(1);
		//for(ArrayList<Integer> i:userTopics.)
	//		sum=sum+i;
	//	System.out.println(IntStream.of(userTopics.values()).sum());

		//config.Config.eta
		
		
		
	}
}
