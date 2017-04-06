package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ7 {
	
	private int[] VALUES;
	
	public DQ7() {
		int len = CollectData.room.length;
		this.VALUES = new int[len];
	}
	
	public void add_Data(int[] _MEDIAN) {
		int index = 0;
		for (int i : _MEDIAN)
			this.VALUES[index++] += i;
	}

	public String toString() {
		double T = 0;
		double B = 0;
		int I = 0;
		for(int i : this.VALUES) {
			T += i * ++I;
			B += i;
		}
		return CollectData.printValue("95TH PERCENTILE: ", (T / B));
	}
	
}