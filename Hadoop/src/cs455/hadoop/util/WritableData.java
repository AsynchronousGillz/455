package cs455.hadoop.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * A custom Writable implementation for Request information.
 *
 * this is simple Custom Writable, and does not implement Comparable or RawComparator
 */
public final class WritableData implements Writable {

	//
	private IntWritable SEGMENT;
	
    // 
	private IntWritable Q1_RENT;
	private IntWritable Q1_OWN;
    
    // 
	private IntWritable Q2_F_MARRIED;
	private IntWritable Q2_M_MARRIED;
	
    public WritableData() {
    	this.SEGMENT = new IntWritable(-1);
        this.Q1_RENT = new IntWritable(-1);
        this.Q1_OWN = new IntWritable(-1);
        this.Q2_F_MARRIED = new IntWritable(-1);
        this.Q2_M_MARRIED = new IntWritable(-1);
    }

    public void write(DataOutput dataOutput) throws IOException {
    	this.SEGMENT.write(dataOutput);
    	this.Q1_RENT.write(dataOutput);
    	this.Q1_OWN.write(dataOutput);
    	this.Q2_F_MARRIED.write(dataOutput);
    	this.Q2_M_MARRIED.write(dataOutput);
    }

	public void readFields(DataInput dataInput) throws IOException {
    	this.SEGMENT.readFields(dataInput);
    	this.Q1_RENT.readFields(dataInput);
    	this.Q1_OWN.readFields(dataInput);
    	this.Q2_F_MARRIED.readFields(dataInput);
    	this.Q2_M_MARRIED.readFields(dataInput);
    }
    
    public IntWritable get_SEGMENT() {
		return SEGMENT;
	}

	public void set_SEGMENT(Integer SEGMENT) {
		this.SEGMENT = new IntWritable(SEGMENT);
	}

	public IntWritable get_RENT() {
		return Q1_RENT;
	}

	public void set_RENT(Integer Q1_RENT) {
		this.Q1_RENT = new IntWritable(Q1_RENT);
	}

	public IntWritable get_OWN() {
		return Q1_OWN;
	}

	public void set_OWN(Integer Q1_OWN) {
		this.Q1_OWN = new IntWritable(Q1_OWN);
	}

	public IntWritable get_F_MARRIED() {
		return Q2_F_MARRIED;
	}

	public void set_F_MARRIED(Integer Q2_F_MARRIED) {
		this.Q2_F_MARRIED = new IntWritable(Q2_F_MARRIED);
	}

	public IntWritable get_M_MARRIED() {
		return Q2_M_MARRIED;
	}

	public void set_M_MARRIED(Integer Q2_M_MARRIED) {
		this.Q2_M_MARRIED = new IntWritable(Q2_M_MARRIED);
	}

    @Override
	public String toString() {
		return "WritableData [SEGMENT=" + SEGMENT + ", Q1_RENT=" + Q1_RENT + ", Q1_OWN=" + Q1_OWN + ", Q2_F_MARRIED="
				+ Q2_F_MARRIED + ", Q2_M_MARRIED=" + Q2_M_MARRIED + "]";
	}
	
}
