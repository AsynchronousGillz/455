package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * On a per-state basis, analyze the distribution of rural households vs. urban households.
 */

public class WQ4 implements Writable {
	
	private IntWritable URBAN;
	private IntWritable RURAL;
	private IntWritable EXTRA;
	
	public WQ4() {
		this.URBAN = new IntWritable(0);
		this.RURAL = new IntWritable(0);
		this.EXTRA = new IntWritable(0);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.RURAL.readFields(arg0);
		this.URBAN.readFields(arg0);
		this.EXTRA.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		this.RURAL.write(arg0);
		this.URBAN.write(arg0);
		this.EXTRA.write(arg0);
	}

	public IntWritable get_URBAN() {
		return this.URBAN;
	}

	public void set_URBAN(String _URBAN) {
		this.URBAN = new IntWritable(Integer.parseInt(_URBAN));
	}

	public IntWritable get_RURAL() {
		return this.RURAL;
	}

	public void set_RURAL(String _RURAL) {
		this.RURAL = new IntWritable(Integer.parseInt(_RURAL));
	}
	
	public IntWritable get_EXTRA() {
		return this.EXTRA;
	}

	public void set_EXTRA(String _EXTRA) {
		this.EXTRA = new IntWritable(Integer.parseInt(_EXTRA));
		
	}

}
