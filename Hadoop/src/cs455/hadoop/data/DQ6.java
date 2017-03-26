package cs455.hadoop.data;

import java.util.ArrayList;

import cs455.hadoop.util.CollectData;

public final class DQ6 {

	ArrayList<Integer> list;
	//
	
	public DQ6() {
		list = new ArrayList<>();
	}
	
	public void add_Median(int _MEDIAN) {
		this.list.add(_MEDIAN);
	}
	
	public String toString() {
		int half = this.list.size() / 2;
		Double ret = Double.valueOf(0);
		if (half % 2 == 0)
			ret += (this.list.get(half) + this.list.get(1+half)) / 2;
		else
			ret += this.list.get(half);
			
		return CollectData.printValue("RENT MEDIAN COST:", ret);
	}
	
}
