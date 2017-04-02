package cs455.hadoop.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import cs455.hadoop.writables.*;

/**
 * A custom Writable implementation for Request information.
 *
 * this is simple Custom Writable, and does not implement Comparable or RawComparator
 */
public final class WritablePrecentileData implements Writable {

	//
	private IntWritable SEGMENT;
	//
	private Text STATE;
	
	//
	public WQ7 Q7;
	public WQ8 Q8;

    public WritablePrecentileData(String state) {
    	this.STATE = new Text(state);
    	this.SEGMENT = new IntWritable(0);
    	this.Q7 = new WQ7();
    	this.Q8 = new WQ8();
    }

    public void write(DataOutput dataOutput) throws IOException {
    	this.STATE.write(dataOutput);
    	this.SEGMENT.write(dataOutput);
    	this.Q7.write(dataOutput);
    	this.Q8.write(dataOutput);
    }

	public void readFields(DataInput dataInput) throws IOException {
		this.STATE.readFields(dataInput);
    	this.SEGMENT.readFields(dataInput);
    	this.Q7.readFields(dataInput);
    	this.Q8.readFields(dataInput);
    }

    public IntWritable get_SEGMENT() {
		return SEGMENT;
	}

	public void set_SEGMENT(String SEGMENT) {
		this.SEGMENT = new IntWritable(Integer.parseInt(SEGMENT));
	}
	
	public String get_STATE() {
		return this.STATE.toString();
	}
	
	public String toText() {
        return "\n" + Q7.toString() + Q8.toString();
	}
	
}
