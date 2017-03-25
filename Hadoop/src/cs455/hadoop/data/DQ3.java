package cs455.hadoop.data;

public final class DQ3 {

	// 
	private Double F_1_AGE;
	private Double F_2_AGE;
	private Double F_3_AGE;
	
	// 
	private Double M_1_AGE;
	private Double M_2_AGE;
	private Double M_3_AGE;
	
	//
	private Double POP;
	
	public DQ3() {
		this.F_1_AGE = Double.valueOf(0);
		this.F_2_AGE = Double.valueOf(0);
		this.F_3_AGE = Double.valueOf(0);
		this.M_1_AGE = Double.valueOf(0);
		this.M_2_AGE = Double.valueOf(0);
		this.M_3_AGE = Double.valueOf(0);
		this.POP = Double.valueOf(0);
	}
	
	public void add_M_1_AGE(int _M_1_AGE) {
		this.M_1_AGE += _M_1_AGE;
	}
	
	public void add_M_2_AGE(int _M_2_AGE) {
		this.M_2_AGE += _M_2_AGE;
	}
	
	public void add_M_3_AGE(int _M_3_AGE) {
		this.M_3_AGE += _M_3_AGE;
	}
	
	public void add_F_1_AGE(int _F_1_AGE) {
		this.F_1_AGE += _F_1_AGE;
	}
	
	public void add_F_2_AGE(int _F_2_AGE) {
		this.F_2_AGE += _F_2_AGE;
	}
	
	public void add_F_3_AGE(int _F_3_AGE) {
		this.F_3_AGE += _F_3_AGE;
	}
	
	public void add_POP(int _POP) {
		this.POP += _POP;
	}
	
	public String toString() {
		String ret = "";
		if (this.POP != Double.valueOf(0)) {
			ret += new String(String.format("   %1$-20s %2$3.2f", "MALE 00-18:", (this.M_1_AGE/this.POP)*100)+"%\n");
			ret += new String(String.format("   %1$-20s %2$3.2f", "MALE 19-29:", (this.M_2_AGE/this.POP)*100)+"%\n");
			ret += new String(String.format("   %1$-20s %2$3.2f", "MALE 30-39:", (this.M_3_AGE/this.POP)*100)+"%\n");
			
        	ret += new String(String.format("   %1$-20s %2$3.2f", "FEMALE 00-18:", (this.F_1_AGE/this.POP)*100)+"%\n");
			ret += new String(String.format("   %1$-20s %2$3.2f", "FEMALE 19-29:", (this.F_2_AGE/this.POP)*100)+"%\n");
			ret += new String(String.format("   %1$-20s %2$3.2f", "FEMALE 30-39:", (this.F_3_AGE/this.POP)*100)+"%\n");
		} else {
			ret += new String(String.format("   %1$-20s", "Population not recorded.")+"\n");
		}
		return ret;
	}
	
}
