package cs455.hadoop.data;

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
			ret += new String(String.format("   %1$-20s %2$3.2f", "RENT:", (this.RENT/this.TOTAL)*100)+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "OWN:", (this.OWN/this.TOTAL)*100)+"%\n");
		} else {
			ret += new String(String.format("   %1$-20s %2$3.2f", "RENT:", Double.valueOf(0))+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "OWN:",  Double.valueOf(0))+"%\n");
		}
		return ret;
	}
	
}
