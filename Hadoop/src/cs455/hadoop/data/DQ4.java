package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ4 {

	// 
	private Double RURAL;
	private Double URBAN;
	private Double EXTRA;
	
	//
	private Double TOTAL;
	
	public DQ4() {
		this.RURAL = Double.valueOf(0);
		this.URBAN = Double.valueOf(0);
		this.EXTRA = Double.valueOf(0);
		this.TOTAL = Double.valueOf(0);
	}
	
	public void add_URBAN(int _URBAN){
		this.URBAN += _URBAN;
		this.TOTAL += _URBAN;
	}
	
	public void add_RURAL(int _RURAL) {
		this.RURAL += _RURAL;
		this.TOTAL += _RURAL;
	}
	
	public void add_EXTRA(int _EXTRA) {
		this.EXTRA += _EXTRA;
		this.TOTAL += _EXTRA;
	}
	
	public String toString() {
		String ret = "";
		if (this.TOTAL != Double.valueOf(0)) {
			ret += CollectData.printPrecent("URBAN:", (this.URBAN/this.TOTAL));
			ret += CollectData.printPrecent("RURAL:", (this.RURAL/this.TOTAL));
			ret += CollectData.printPrecent("UNDEFINED:", (this.EXTRA/this.TOTAL));
		} else {
			ret += new String(String.format("   %1$-20s", "Housing Status not recorded.")+"\n");
		}
		return ret;
	}
	
}
