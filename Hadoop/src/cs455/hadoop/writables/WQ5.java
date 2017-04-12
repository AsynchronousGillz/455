package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import cs455.hadoop.util.CollectData;

/**
 * On a per-state basis, what is the median value of the house that occupied by
 * owners?
 */

public class WQ5 implements Writable {

	private IntWritable[] VALUES;

	public WQ5() {
		int len = CollectData.owner.length;
		this.VALUES = new IntWritable[len];
		for (int i = 0; i < len; i++)
			this.VALUES[i] = new IntWritable(0);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		int len = CollectData.owner.length;
		for (int i = 0; i < len; i++)
			this.VALUES[i].readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		int len = CollectData.owner.length;
		for (int i = 0; i < len; i++)
			this.VALUES[i].write(arg0);
	}

	public int[] get_Median() {
		int[] ret = new int[this.VALUES.length];
		int index = 0;
		for (IntWritable i : this.VALUES)
			ret[index++] = i.get();
		return ret;
	}

	public void set_Median(Text value, int start, int size) {
		int len = CollectData.owner.length;
		for (int i = 0; i < len; i++, start += size)
			this.VALUES[i] = new IntWritable(Integer.parseInt(value.toString().substring(start, (start + size))));
	}

}
