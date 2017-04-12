package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * On a per-state basis, analyze the age distribution (of the population that identifies themselves as Hispanic) based on gender. 
 * (a). Percentage of people below 18 years (inclusive) old.
 * (b). Percentage of people between 19 (inclusive) and 29 (inclusive) years old.
 * (c). Percentage of people between 30 (inclusive) and 39 (inclusive) years old. 
 */

public final class WQ3 implements Writable {

	// 
	private IntWritable M_1_AGE;
	private IntWritable M_2_AGE;
	private IntWritable M_3_AGE;
	//
	private IntWritable M_TOTAL;
	
	// 
	private IntWritable F_1_AGE;
	private IntWritable F_2_AGE;
	private IntWritable F_3_AGE;
	//
	private IntWritable F_TOTAL;
	
	public WQ3() {
		this.M_1_AGE = new IntWritable(0);
		this.M_2_AGE = new IntWritable(0);
		this.M_3_AGE = new IntWritable(0);
		this.M_TOTAL = new IntWritable(0);
    	this.F_1_AGE = new IntWritable(0);
    	this.F_2_AGE = new IntWritable(0);
    	this.F_3_AGE = new IntWritable(0);
    	this.F_TOTAL = new IntWritable(0);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.M_1_AGE.readFields(arg0);
		this.M_2_AGE.readFields(arg0);
		this.M_3_AGE.readFields(arg0);
		this.M_TOTAL.readFields(arg0);
		this.F_1_AGE.readFields(arg0);
		this.F_2_AGE.readFields(arg0);
		this.F_3_AGE.readFields(arg0);
		this.F_TOTAL.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		this.M_1_AGE.write(arg0);
		this.M_2_AGE.write(arg0);
		this.M_3_AGE.write(arg0);
		this.M_TOTAL.write(arg0);
		this.F_1_AGE.write(arg0);
		this.F_2_AGE.write(arg0);
		this.F_3_AGE.write(arg0);
		this.F_TOTAL.write(arg0);
	}
	
	public IntWritable get_M_AGE_1() {
		return M_1_AGE;
	}

	public IntWritable get_M_AGE_2() {
		return M_2_AGE;
	}
	
	public IntWritable get_M_AGE_3() {
		return M_3_AGE;
	}
	
	public IntWritable get_M_TOTAL() {
		return M_TOTAL;
	}

	public void set_M_AGE_1(String AGE_1) {
		this.M_1_AGE = new IntWritable(Integer.parseInt(AGE_1));
	}
	
	public void set_M_AGE_2(String AGE_2) {
		this.M_2_AGE = new IntWritable(Integer.parseInt(AGE_2));
	}
	
	public void set_M_AGE_3(String AGE_3) {
		this.M_3_AGE = new IntWritable(Integer.parseInt(AGE_3));
	}
	
	public void set_M_TOTAL(String TOTAL) {
		this.M_TOTAL = new IntWritable(Integer.parseInt(TOTAL));
	}
	
	public IntWritable get_F_AGE_1() {
		return F_1_AGE;
	}

	public IntWritable get_F_AGE_2() {
		return F_2_AGE;
	}
	
	public IntWritable get_F_AGE_3() {
		return F_3_AGE;
	}
	
	public IntWritable get_F_TOTAL() {
		return F_TOTAL;
	}
	
	public void set_F_AGE_1(String AGE_1) {
		this.F_1_AGE = new IntWritable(Integer.parseInt(AGE_1));
	}
	
	public void set_F_AGE_2(String AGE_2) {
		this.F_2_AGE = new IntWritable(Integer.parseInt(AGE_2));
	}
	
	public void set_F_AGE_3(String AGE_3) {
		this.F_3_AGE = new IntWritable(Integer.parseInt(AGE_3));
	}
	
	public void set_F_TOTAL(String TOTAL) {
		this.F_TOTAL = new IntWritable(Integer.parseInt(TOTAL));
	}

	@Override
	public String toString() {
		return "WQ3 [M_1_AGE=" + M_1_AGE + ", M_2_AGE=" + M_2_AGE + ", M_3_AGE=" + M_3_AGE + ", M_TOTAL=" + M_TOTAL
				+ ", F_1_AGE=" + F_1_AGE + ", F_2_AGE=" + F_2_AGE + ", F_3_AGE=" + F_3_AGE + ", F_TOTAL=" + F_TOTAL
				+ "]";
	}

}
