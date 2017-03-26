package cs455.hadoop.data;

import java.util.ArrayList;

public final class DQ5 {

	ArrayList<Integer> list;
	//
	
	public DQ5() {
		list = new ArrayList<>();
	}
	
	public void add_Median(int _MEDIAN) {
		this.list.add(_MEDIAN);
	}
	
	public String toString() {
		int half = this.list.size() / 2;
		Integer ret = 0;
		if (half % 2 == 0)
			ret = this.list.get(half);
		else 
			ret = this.list.get((this.list.get(half) / this.list.get(1+half)) / 2);
		return new String(String.format("   %1$-20s %2$3.2f", "URBAN:", ret)+"%\n");
	}
	
}
