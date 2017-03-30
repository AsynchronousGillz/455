package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ6 {
	
	private int[] list;
	//
	
	public DQ6() {
		this.list = new int[CollectData.rent.length];
	}
	
	public void add_Median(int _MEDIAN) {
		list[_MEDIAN] += 1;
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
		return CollectData.printText("RENT MEDIAN COST:", CollectData.rent[index]);
	}
	
}
