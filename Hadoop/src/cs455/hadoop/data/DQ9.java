package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

/**
 * Race owned vs rented H9. Tenure by Race of Householder Owner occupied: White
 * - 6798, Black - 6807, Native American - 6816, Asian or Pacific Islander - 6825,
 * Other - 6834
 * 
 * Renter occupied: White - 6843, Black - 6852, Native American - 6861, Asian or
 * Pacific Islander - 6870, Other - 6879
 * 
 */

public final class DQ9 {

	// 
	private Double OLD_POP;
	private Double TOTAL_POP;
	
	public DQ9() {
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
