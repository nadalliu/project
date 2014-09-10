package dataformat;

/**
 * @author Weinan Zhang
 * 10 Dec 2012
 */
public class UserItemRateUnit {

	public final int uid;
	public final int iid;
	public final float rate;

	public UserItemRateUnit(int u, int i, float r){
		uid = u;
		iid = i;
		rate = r;
	}
}
