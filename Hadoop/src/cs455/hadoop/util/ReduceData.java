package cs455.hadoop.util;

public final class ReduceData {

	// 
	private Double Q1_RENT;
	private Double Q1_OWN;
	private Double Q1_TOTAL;
    
    // 
	private Double Q2_F_MARRIED;
	private Double Q2_M_MARRIED;
	private Double Q2_TOTAL;
	
	
	public ReduceData() {
		this.Q1_RENT = this.Q1_OWN = this.Q1_TOTAL = this.Q2_F_MARRIED = this.Q2_M_MARRIED = this.Q2_TOTAL = Double.valueOf(0);
	}

	public void add_Q1_RENT(int _Q1_RENT) {
		this.Q1_RENT += _Q1_RENT;
		this.Q1_TOTAL += _Q1_RENT;
	}

	public void add_Q1_OWN(int _Q1_OWN) {
		this.Q1_OWN += _Q1_OWN;
		this.Q1_TOTAL += _Q1_OWN;
	}

	public void add_Q2_F_MARRIED(int _Q2_F_MARRIED) {
		this.Q2_F_MARRIED += _Q2_F_MARRIED;
		this.Q2_TOTAL += _Q2_F_MARRIED;
	}

	public void add_Q2_M_MARRIED(int _Q2_M_MARRIED) {
		this.Q2_M_MARRIED += _Q2_M_MARRIED;
		this.Q2_TOTAL += _Q2_M_MARRIED;
	}

	public String toString() {
		String ret = "\n";
		if (this.Q1_TOTAL != Double.valueOf(0)) {
			ret += new String(String.format("   %1$-20s %2$3.2f", "RENT:", (this.Q1_RENT/this.Q1_TOTAL)*100)+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "OWN:", (this.Q1_OWN/this.Q1_TOTAL)*100)+"%\n");
		} else {
			ret += new String(String.format("   %1$-20s %2$3.2f", "RENT:", Double.valueOf(0))+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "OWN:",  Double.valueOf(0))+"%\n");
		}
		if (this.Q2_TOTAL != Double.valueOf(0)) {
			ret += new String(String.format("   %1$-20s %2$3.2f", "F NEVER MARRIED: ", (this.Q2_F_MARRIED/this.Q2_TOTAL)*100)+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "M NEVER MARRIED: ", (this.Q2_M_MARRIED/this.Q2_TOTAL)*100)+"%\n");
		} else { 	
			ret += new String(String.format("   %1$-20s %2$3.2f", "F NEVER MARRIED: ", Double.valueOf(0))+"%\n");
        	ret += new String(String.format("   %1$-20s %2$3.2f", "M NEVER MARRIED: ", Double.valueOf(0))+"%\n");
		}
        return ret;
	}
	
}
