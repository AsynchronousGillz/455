package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ2 {

	// 
	private Double F_MARRIED;
	private Double M_MARRIED;
	private Double TOTAL_POP;
	
	public DQ2() {
		this.F_MARRIED = Double.valueOf(0);
		this.M_MARRIED = Double.valueOf(0);
		this.TOTAL_POP = Double.valueOf(0);
	}
	
	public void add_F_MARRIED(int _F_MARRIED) {
		this.F_MARRIED += _F_MARRIED;
	}

	public void add_M_MARRIED(int _M_MARRIED) {
		this.M_MARRIED += _M_MARRIED;
	}
	
	public void add_POP(int _POP) {
		this.TOTAL_POP += _POP;
	}
	
	public String toString() {
		String ret = "";
		if (this.TOTAL_POP != Double.valueOf(0)) {
			ret += CollectData.printPrecent("F NEVER MARRIED: ", (this.F_MARRIED/this.TOTAL_POP));
        	ret += CollectData.printPrecent("M NEVER MARRIED: ", (this.M_MARRIED/this.TOTAL_POP));
		} else { 	
			ret += CollectData.printPrecent("F NEVER MARRIED: ", Double.valueOf(0));
        	ret += CollectData.printPrecent("M NEVER MARRIED: ", Double.valueOf(0));
		}
		return ret;
	}
	
}
