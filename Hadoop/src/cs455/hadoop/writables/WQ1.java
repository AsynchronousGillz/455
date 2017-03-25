package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * On a per-state basis provide a breakdown of the percentage of residences that were rented vs. owned.
 */

public final class WQ1 implements Writable {
	
	// 
	private IntWritable Q1_RENT;
	private IntWritable Q1_OWN;
	
	public WQ1() {
		this.Q1_RENT = new IntWritable(0);
		this.Q1_OWN = new IntWritable(0);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.Q1_OWN.readFields(arg0);
		this.Q1_RENT.readFields(arg0);
	}
	@Override
	public void write(DataOutput arg0) throws IOException {
		this.Q1_OWN.write(arg0);
		this.Q1_RENT.write(arg0);
	}
	
	public IntWritable get_RENT() {
		return Q1_RENT;
	}
	
	public IntWritable get_OWN() {
		return Q1_OWN;
	}
	
	public void set_R(String Q1_RENT) {
		this.Q1_RENT = new IntWritable(Integer.parseInt(Q1_RENT));
	}
	
	public void set_O(String Q1_OWN) {
		this.Q1_OWN = new IntWritable(Integer.parseInt(Q1_OWN));
	}

	public String toString() {
		return "WQ1 [Q1_RENT=" + Q1_RENT + ", Q1_OWN=" + Q1_OWN + "]";
	}

}
