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
public final class WritableStateData implements Writable {

	//
	private IntWritable SEGMENT;
	//
	private IntWritable POPULATION;
	
	//
	public WQ1 Q1;
	public WQ2 Q2;
	public WQ3 Q3;
	public WQ4 Q4;
	public WQ5 Q5;
	public WQ6 Q6;
	public WQ7 Q7;
	public WQ8 Q8;
	public WQ9 Q9;

    public WritableStateData() {
    	this.SEGMENT = new IntWritable(0);
    	this.POPULATION = new IntWritable(0);
    	this.Q1 = new WQ1();
    	this.Q2 = new WQ2();
    	this.Q3 = new WQ3();
    	this.Q4 = new WQ4();
    	this.Q5 = new WQ5();
    	this.Q6 = new WQ6();
    	this.Q7 = new WQ7();
    	this.Q8 = new WQ8();
    	this.Q9 = new WQ9();
    }

    public void write(DataOutput dataOutput) throws IOException {
    	this.POPULATION.write(dataOutput);
    	this.SEGMENT.write(dataOutput);
    	this.Q1.write(dataOutput);
    	this.Q2.write(dataOutput);
    	this.Q3.write(dataOutput);
    	this.Q4.write(dataOutput);
    	this.Q5.write(dataOutput);
    	this.Q6.write(dataOutput);
    	this.Q7.write(dataOutput);
    	this.Q8.write(dataOutput);
    	this.Q9.write(dataOutput);
    }

	public void readFields(DataInput dataInput) throws IOException {
		this.POPULATION.readFields(dataInput);
    	this.SEGMENT.readFields(dataInput);
    	this.Q1.readFields(dataInput);
    	this.Q2.readFields(dataInput);
    	this.Q3.readFields(dataInput);
    	this.Q4.readFields(dataInput);
    	this.Q5.readFields(dataInput);
    	this.Q6.readFields(dataInput);
    	this.Q7.readFields(dataInput);
    	this.Q8.readFields(dataInput);
    	this.Q9.readFields(dataInput);
    }

    public IntWritable get_SEGMENT() {
		return SEGMENT;
	}

	public void set_SEGMENT(String SEGMENT) {
		this.SEGMENT = new IntWritable(Integer.parseInt(SEGMENT));
	}
	
	public IntWritable get_POPULATION() {
		return POPULATION;
	}
	
	public void set_POPULATION(String _POPULATION) {
		this.POPULATION = new IntWritable(Integer.parseInt(_POPULATION));
	}
	
	public String toText() {
        return "\n" + Q1.toString() + Q2.toString() + Q3.toString() + Q4.toString()
        + Q5.toString() + Q6.toString() + Q7.toString() + Q8.toString() + Q9.toString();
	}
	
}
