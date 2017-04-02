 package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import cs455.hadoop.util.CollectData;

/**
 * What is the 95th percentile of the average number of rooms per house across all states? 
 */

public class WQ7 implements Writable {

	private IntWritable[] VALUES;

	public WQ7() {
		int len = CollectData.room.length;
		this.VALUES = new IntWritable[len];
		for (int i = 0; i < len; i++)
			VALUES[i] = new IntWritable(0);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		for (IntWritable i : this.VALUES)
			i.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		for (IntWritable i : this.VALUES)
			i.write(arg0);
	}

	public int[] get_Percentile() {
		int[] ret = new int[this.VALUES.length];
		int index = 0;
		for (IntWritable i : this.VALUES)
			ret[index++] = i.get();
		return ret;
	}

	public void set_Percentile(Text value, int start, int size) {
		int len = CollectData.room.length;
		for (int i = 0; i < len; i++, start += size) 
			this.VALUES[i] = new IntWritable(Integer.parseInt(value.toString().substring(start, (start + size))));
	}

}
