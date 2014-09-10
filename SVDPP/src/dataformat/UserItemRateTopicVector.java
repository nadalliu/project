package dataformat;

public class UserItemRateTopicVector {

	public final int uid;
	public final int iid;
	public final float rate;
	public final double[] topic;
	public final int topic_num;

	public UserItemRateTopicVector(int u, int i, float r, double[] t, int tn){
		uid = u;
		iid = i;
		rate = r;
		topic = t;
		topic_num=tn;
	}
}