package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * On a per-state basis, what is the median value of the house that occupied by owners? 
 */

public class WQ5 implements Writable {
	
	private IntWritable MEDIAN;

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.MEDIAN.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		this.MEDIAN.write(arg0);
	}
	
	public IntWritable get_Median() {
		return this.MEDIAN;
	}

	public void set_Median(String _MEDIAN) {
		this.MEDIAN = new IntWritable(Integer.parseInt(_MEDIAN));
	}

}
