package cs455.hadoop.util;

import org.apache.hadoop.io.Text;

import cs455.hadoop.data.*;

public final class ReduceStateData {

    public DQ1 Q1;
    public DQ2 Q2;
    public DQ3 Q3;
    public DQ4 Q4;
    public DQ5 Q5;
    public DQ6 Q6;
    public DQ7 Q7;
    public DQ8 Q8;
	
	public ReduceStateData() {
		this.Q1 = new DQ1();
		this.Q2 = new DQ2();
		this.Q3 = new DQ3();
		this.Q4 = new DQ4();
		this.Q5 = new DQ5();
		this.Q6 = new DQ6();
		this.Q7 = new DQ7();
		this.Q8 = new DQ8();
	}
	
	public String toString() {
        return "\n" + Q1.toString() + Q2.toString() + Q3.toString() + Q4.toString()
        + Q5.toString() + Q6.toString() + Q7.toString() + Q8.toString();
	}
	
	public Text toText() {
        return new Text(this.toString());
	}
	
}
