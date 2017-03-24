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
	//
	private IntWritable POPULATION;
	
	//
	public WQ1 Q1;
	public WQ2 Q2;
	public WQ3 Q3;

    public WritableData() {
    	this.SEGMENT = new IntWritable(0);
    	this.POPULATION = new IntWritable(0);
    	this.Q1 = new WQ1();
    	this.Q2 = new WQ2();
    	this.Q3 = new WQ3();
    }

    public void write(DataOutput dataOutput) throws IOException {
    	this.POPULATION.write(dataOutput);
    	this.SEGMENT.write(dataOutput);
    	this.Q1.write(dataOutput);
    	this.Q2.write(dataOutput);
    	this.Q3.write(dataOutput);
    }

	public void readFields(DataInput dataInput) throws IOException {
		this.POPULATION.readFields(dataInput);
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
	
	public IntWritable get_POPULATION() {
		return POPULATION;
	}
	
	public void set_POPULATION(String _POPULATION) {
		this.POPULATION = new IntWritable(Integer.parseInt(_POPULATION));
	}
	
	public String toText() {
        return "\n" + Q1.toString() + Q2.toString() + Q3.toString();
	}
	
}
