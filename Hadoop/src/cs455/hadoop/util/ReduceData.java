package cs455.hadoop.util;

import cs455.hadoop.data.*;

public final class ReduceData {

    private DQ1 DQ1;
    private DQ2 DQ2;
    private DQ3 DQ3;
	
	public ReduceData() {
		this.DQ1 = new DQ1();
		this.DQ2 = new DQ2();
		this.DQ3 = new DQ3();
	}
	
	
	
	
	

	public String toString() {
		String ret = "\n";
		ret += DQ1.toString();
		ret += DQ2.toString();
		ret += DQ3.toString();
        return ret;
	}
	
}
