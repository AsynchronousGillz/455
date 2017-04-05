package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * Which state has the highest percentage of elderly people (age > 85) in their population? 
 */

public class WQ8 implements Writable {

	//
	private IntWritable OLD_POP;

	public WQ8() {
		this.OLD_POP = new IntWritable(0);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.OLD_POP.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		this.OLD_POP.write(arg0);
	}

	public IntWritable get_OLD_POP() {
		return OLD_POP;
	}

	public void set_OLD_POP(String _OLD_POP) {
		this.OLD_POP = new IntWritable(Integer.parseInt(_OLD_POP));
	}

}
