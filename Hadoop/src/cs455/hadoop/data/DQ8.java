package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ8 {

	// 
	private Double OLD_POP;
	private Double TOTAL_POP;
	
	public DQ8() {
		this.OLD_POP = Double.valueOf(0);
		this.TOTAL_POP = Double.valueOf(0);
	}
	
	public void add_OLD(int _OLD_POP) {
		this.OLD_POP += _OLD_POP;
	}

	public void add_POP(int _POP) {
		this.TOTAL_POP += _POP;
	}
	
	public String toString() {
		return CollectData.printPrecent("PRECENTAGE OF ELDERLY:", (this.OLD_POP/this.TOTAL_POP));
	}
	
}
