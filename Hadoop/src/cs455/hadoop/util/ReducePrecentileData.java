package cs455.hadoop.util;

import org.apache.hadoop.io.Text;

import cs455.hadoop.data.*;

public final class ReducePrecentileData {

    public DQ7 Q7;
    public DQ8 Q8;
	
	public ReducePrecentileData() {
		this.Q7 = new DQ7();
		this.Q8 = new DQ8();
	}
	
	public String toString() {
        return "\n" + Q7.toString() + Q8.toString();
	}
	
	public Text toText() {
        return new Text(this.toString());
	}
	
}
