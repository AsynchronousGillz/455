package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ7 {
	
	private int[] list;
	//
	
	public DQ7() {
		this.list = new int[CollectData.room.length];
	}
	
	public void add_Median(int[] _MEDIAN) {
		int index = 0;
		for (int i : _MEDIAN)
			list[index++] += i;
	}
	
	public String toString() {
		double listTotal = 0;
		for(int i : list) {
			listTotal += i;
		}
		double valueIndex = listTotal / 2;
		int index = 0;
		for(int i : list) {
			valueIndex -= i;
			if (valueIndex <= 0)
				break;
			index++;
		}
		return CollectData.printText("95th PRECENTILE:", CollectData.room[index]);
	}
	
}
