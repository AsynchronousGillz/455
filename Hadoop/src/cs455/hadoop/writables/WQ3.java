package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public final class WQ3 implements Writable {

	// 
	private IntWritable M_1_AGE;
	private IntWritable M_2_AGE;
	private IntWritable M_3_AGE;
	// 
	private IntWritable F_1_AGE;
	private IntWritable F_2_AGE;
	private IntWritable F_3_AGE;
	
	public WQ3() {
		this.M_1_AGE = this.M_2_AGE = this.M_3_AGE = new IntWritable(-1);
    	this.F_1_AGE = this.F_2_AGE = this.F_3_AGE = new IntWritable(-1);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.M_1_AGE.readFields(arg0);
		this.M_2_AGE.readFields(arg0);
		this.M_3_AGE.readFields(arg0);
		this.F_1_AGE.readFields(arg0);
		this.F_2_AGE.readFields(arg0);
		this.F_3_AGE.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		this.M_1_AGE.write(arg0);
		this.M_2_AGE.write(arg0);
		this.M_3_AGE.write(arg0);
		this.F_1_AGE.write(arg0);
		this.F_2_AGE.write(arg0);
		this.F_3_AGE.write(arg0);
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

	public void set_M_Q3(String AGE_1, String AGE_2, String AGE_3) {
		this.M_1_AGE = new IntWritable(Integer.parseInt(AGE_1));
		this.M_2_AGE = new IntWritable(Integer.parseInt(AGE_2));
		this.M_3_AGE = new IntWritable(Integer.parseInt(AGE_3));
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

	public void set_F_Q3(String AGE_1, String AGE_2, String AGE_3) {
		this.F_1_AGE = new IntWritable(Integer.parseInt(AGE_1));
		this.F_2_AGE = new IntWritable(Integer.parseInt(AGE_2));
		this.F_3_AGE = new IntWritable(Integer.parseInt(AGE_3));
	}

}
