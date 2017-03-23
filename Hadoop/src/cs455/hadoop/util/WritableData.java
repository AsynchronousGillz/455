package cs455.hadoop.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import cs455.hadoop.writables.*;

/**
 * A custom Writable implementation for Request information.
 *
 * this is simple Custom Writable, and does not implement Comparable or RawComparator
 */
public final class WritableData implements Writable {

	//
	private IntWritable SEGMENT;
	
	private WQ1 Q1;
	private WQ2 Q2;
	private WQ3 Q3;
	
    public WritableData() {
    	this.SEGMENT = new IntWritable(-1); 
    	this.Q1 = new WQ1();
    	this.Q2 = new WQ2();
    	this.Q3 = new WQ3();
    }

    public void write(DataOutput dataOutput) throws IOException {
    	this.SEGMENT.write(dataOutput);
    	this.Q1.write(dataOutput);
    	this.Q2.write(dataOutput);
    	this.Q3.write(dataOutput);
    }

	public void readFields(DataInput dataInput) throws IOException {
    	this.SEGMENT.readFields(dataInput);
    	this.Q1.readFields(dataInput);
    	this.Q2.readFields(dataInput);
    	this.Q3.readFields(dataInput);
    }
    
    public IntWritable get_SEGMENT() {
		return SEGMENT;
	}

	public void set_SEGMENT(String SEGMENT) {
		this.SEGMENT = new IntWritable(Integer.parseInt(SEGMENT));
	}
	
	public void set_Q1(String Q1_RENT, String Q1_OWN) {
		this.Q1.set_Q1(Q1_RENT, Q1_OWN);
	}
	
	public void set_Q2(String M_MARRIED, String F_MARRIED) {
		this.Q2.set_Q2(M_MARRIED, F_MARRIED);
	}
	
	public void set_POPULATION(String _POPULATION) {
		this.Q2.set_POPULATION(_POPULATION);
	}
	
	public void set_M_Q3(String AGE_1, String AGE_2, String AGE_3) {
		// TODO
	}
	
	public void set_F_Q3(String AGE_1, String AGE_2, String AGE_3) {
		// TODO
	}
	
}
