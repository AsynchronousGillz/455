package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * Gender precent of elderly people (age > 85) in each state? 
 */

public class WQ9 implements Writable {

	// 
		private IntWritable OLD_POP;
		private IntWritable TOTAL_POP;
		
		public WQ9() {
			this.OLD_POP = new IntWritable(0);
			this.TOTAL_POP = new IntWritable(0);
		}
		
		@Override
		public void readFields(DataInput arg0) throws IOException {
			this.OLD_POP.readFields(arg0);
			this.TOTAL_POP.readFields(arg0);
		}
		
		@Override
		public void write(DataOutput arg0) throws IOException {
			this.OLD_POP.write(arg0);
			this.TOTAL_POP.write(arg0);
		}
		
		
		public IntWritable get_OLD_POP() {
			return OLD_POP;
		}

		public IntWritable get_TOTAL_POP() {
			return TOTAL_POP;
		}

		public void set_OLD_POP(String _OLD_POP) {
			this.OLD_POP = new IntWritable(Integer.parseInt(_OLD_POP));
		}
		
		public void set_TOTAL_POP(String _TOTAL_POP) {
			this.TOTAL_POP = new IntWritable(Integer.parseInt(_TOTAL_POP));
		}


}
