package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ7 {
	
	private int[] VALUES;
	private int COUNT;
	
	public DQ7() {
		int len = CollectData.room.length;
		this.VALUES = new int[len];
		this.COUNT = 0;
	}
	
	public void add_Data(int[] _MEDIAN) {
		int index = 0;
		for (int i : _MEDIAN)
			this.VALUES[index++] += i;
		this.COUNT += 1;
	}

	public String toString() {
		int len = CollectData.room.length;
		double avg = 0;
		for(int i : this.VALUES)
			avg += ((double)(0 + i) / this.COUNT);
		return CollectData.printValue("95TH PERCENTILE: ", (avg/len)*100);
	}
	
}
