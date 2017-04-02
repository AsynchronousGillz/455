package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ3 {

	// 
	private Double F_1_AGE;
	private Double F_2_AGE;
	private Double F_3_AGE;
	//
	private Double F_POP;
	
	// 
	private Double M_1_AGE;
	private Double M_2_AGE;
	private Double M_3_AGE;
	//
	private Double M_POP;
	
	public DQ3() {
		this.F_1_AGE = Double.valueOf(0);
		this.F_2_AGE = Double.valueOf(0);
		this.F_3_AGE = Double.valueOf(0);
		this.F_POP = Double.valueOf(0);
		this.M_1_AGE = Double.valueOf(0);
		this.M_2_AGE = Double.valueOf(0);
		this.M_3_AGE = Double.valueOf(0);
		this.M_POP = Double.valueOf(0);
	}
	
	public void add_M_1_AGE(int _M_1_AGE) {
		this.M_1_AGE += _M_1_AGE;
		this.M_POP += _M_1_AGE;
	}
	
	public void add_M_2_AGE(int _M_2_AGE) {
		this.M_2_AGE += _M_2_AGE;
		this.M_POP += _M_2_AGE;
	}
	
	public void add_M_3_AGE(int _M_3_AGE) {
		this.M_3_AGE += _M_3_AGE;
		this.M_POP += _M_3_AGE;
	}
	
	public void add_M_POP(int _POP) {
		this.M_POP += _POP;
	}
	
	public void add_F_1_AGE(int _F_1_AGE) {
		this.F_1_AGE += _F_1_AGE;
		this.F_POP += _F_1_AGE;
	}
	
	public void add_F_2_AGE(int _F_2_AGE) {
		this.F_2_AGE += _F_2_AGE;
		this.F_POP += _F_2_AGE;
	}
	
	public void add_F_3_AGE(int _F_3_AGE) {
		this.F_3_AGE += _F_3_AGE;
		this.F_POP += _F_3_AGE;
	}
	
	public void add_F_POP(int _POP) {
		this.F_POP += _POP;
	}
	
	public String toString() {
		String ret = "";
		ret += CollectData.printPrecent("HISPANIC MALE 00-18:", (this.M_1_AGE/this.M_POP));
		ret += CollectData.printPrecent("HISPANIC MALE 19-29:", (this.M_2_AGE/this.M_POP));
		ret += CollectData.printPrecent("HISPANIC MALE 30-39:", (this.M_3_AGE/this.M_POP));
		
    	ret += CollectData.printPrecent("HISPANIC FEMALE 00-18:", (this.F_1_AGE/this.F_POP));
		ret += CollectData.printPrecent("HISPANIC FEMALE 19-29:", (this.F_2_AGE/this.F_POP));
		ret += CollectData.printPrecent("HISPANIC FEMALE 30-39:", (this.F_3_AGE/this.F_POP));
		return ret;
	}
	
}
