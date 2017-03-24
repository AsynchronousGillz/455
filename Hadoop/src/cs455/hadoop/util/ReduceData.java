package cs455.hadoop.util;

import org.apache.hadoop.io.Text;

import cs455.hadoop.data.*;

public final class ReduceData {

    public DQ1 Q1;
    public DQ2 Q2;
    public DQ3 Q3;
	
	public ReduceData() {
		this.Q1 = new DQ1();
		this.Q2 = new DQ2();
		this.Q3 = new DQ3();
	}
	
	public String toString() {
        return "\n" + Q1.toString() + Q2.toString() + Q3.toString();
	}
	
	public Text toText() {
        return new Text("\n" + Q1.toString() + Q2.toString() + Q3.toString());
	}
	
}
