package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * On a per-state basis what percentage of the population never married?  
 * Report this for both males and females.  
 * Note: The US Census data tracks this information for persons with ages 15 years and over. 
 */

public final class WQ2 implements Writable {
	
	// 
	private IntWritable F_MARRIED;
	private IntWritable M_MARRIED;
	
	public WQ2() {
		this.F_MARRIED = new IntWritable(0);
		this.M_MARRIED = new IntWritable(0);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.F_MARRIED.readFields(arg0);
		this.M_MARRIED.readFields(arg0);
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		this.F_MARRIED.write(arg0);
		this.M_MARRIED.write(arg0);
	}
	
	
	public IntWritable get_F_MARRIED() {
		return F_MARRIED;
	}

	public IntWritable get_M_MARRIED() {
		return M_MARRIED;
	}

	public void set_M(String M_MARRIED) {
		this.M_MARRIED = new IntWritable(Integer.parseInt(M_MARRIED));
	}
	
	public void set_F(String F_MARRIED) {
		this.F_MARRIED = new IntWritable(Integer.parseInt(F_MARRIED));
	}

	@Override
	public String toString() {
		return "WQ2 [F_MARRIED=" + F_MARRIED + ", M_MARRIED=" + M_MARRIED + "]";
	}

}
