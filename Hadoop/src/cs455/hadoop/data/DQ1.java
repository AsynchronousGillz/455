package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ1 {

	// 
	private Double RENT;
	private Double OWN;
	private Double TOTAL;
	
	public DQ1() {
		this.RENT = Double.valueOf(0);
		this.OWN = Double.valueOf(0);
		this.TOTAL = Double.valueOf(0);
	}
	
	public void add_RENT(int _RENT) {
		this.RENT += _RENT;
		this.TOTAL += _RENT;
	}

	public void add_OWN(int _OWN) {
		this.OWN += _OWN;
		this.TOTAL += _OWN;
	}
	
	public String toString() {
		String ret = "";
		if (this.TOTAL != Double.valueOf(0)) {
			ret += CollectData.printPrecent("RENT:", (this.RENT/this.TOTAL));
        	ret += CollectData.printPrecent("OWN:", (this.OWN/this.TOTAL));
		} else {
			ret += CollectData.printPrecent("RENT:", Double.valueOf(0));
        	ret += CollectData.printPrecent("OWN:",  Double.valueOf(0));
		}
		return ret;
	}
	
}
