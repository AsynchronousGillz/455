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
    public DQ9 Q9;
	
	public ReduceStateData() {
		this.Q1 = new DQ1();
		this.Q2 = new DQ2();
		this.Q3 = new DQ3();
		this.Q4 = new DQ4();
		this.Q5 = new DQ5();
		this.Q6 = new DQ6();
		this.Q7 = new DQ7();
		this.Q8 = new DQ8();
		this.Q9 = new DQ9();
	}
	
	public String toString() {
        return new StringBuilder().append("\n").append(Q1.toString()).append(Q2.toString()).append(Q3.toString()).append(Q4.toString())
        .append(Q5.toString()).append(Q6.toString()).append(Q7.toString()).append(Q8.toString()).append(Q9.toString()).toString();
	}
	
	public Text toText() {
        return new Text(this.toString());
	}
	
}
