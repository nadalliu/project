package dataformat;

public class UserItemRateTopicUnit {

	public final int uid;
	public final int iid;
	public final float rate;
	public final int topic;

	public UserItemRateTopicUnit(int u, int i, float r, int t){
		uid = u;
		iid = i;
		rate = r;
		topic = t;
	}
}