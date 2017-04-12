package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * Race owned vs rented H9. Tenure by Race of Householder Owner occupied: White
 * - 6798, Black - 6807, Native American - 6816, Asian or Pacific Islander - 6825,
 * Other - 6834
 * 
 * Renter occupied: White - 6843, Black - 6852, Native American - 6861, Asian or
 * Pacific Islander - 6870, Other - 6879
 */

public class WQ9 implements Writable {

	// 
	private IntWritable[] WHITE;
	private IntWritable[] BLACK;
	private IntWritable[] NATIV;
	private IntWritable[] ASIAN;
	private IntWritable[] OTHER;
	
	public WQ9() {
		this.WHITE = new IntWritable[2];
		this.WHITE[0] = new IntWritable(0);
		this.WHITE[1] = new IntWritable(0);
		
		this.BLACK = new IntWritable[2];
		this.BLACK[0] = new IntWritable(0);
		this.BLACK[1] = new IntWritable(0);
		
		this.NATIV = new IntWritable[2];
		this.NATIV[0] = new IntWritable(0);
		this.NATIV[1] = new IntWritable(0);
		
		this.ASIAN = new IntWritable[2];
		this.ASIAN[0] = new IntWritable(0);
		this.ASIAN[1] = new IntWritable(0);
		
		this.OTHER = new IntWritable[2];
		this.OTHER[0] = new IntWritable(0);
		this.OTHER[1] = new IntWritable(0);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.WHITE[0].readFields(arg0);
		this.WHITE[1].readFields(arg0);
		this.BLACK[0].readFields(arg0);
		this.BLACK[1].readFields(arg0);
		this.NATIV[0].readFields(arg0);
		this.NATIV[1].readFields(arg0);
		this.ASIAN[0].readFields(arg0);
		this.ASIAN[1].readFields(arg0);
		this.OTHER[0].readFields(arg0);
		this.OTHER[1].readFields(arg0);
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		this.WHITE[0].write(arg0);
		this.WHITE[1].write(arg0);
		this.BLACK[0].write(arg0);
		this.BLACK[1].write(arg0);
		this.NATIV[0].write(arg0);
		this.NATIV[1].write(arg0);
		this.ASIAN[0].write(arg0);
		this.ASIAN[1].write(arg0);
		this.OTHER[0].write(arg0);
		this.OTHER[1].write(arg0);
	}
	
	
	public IntWritable get_WHITE_OWN() {
		return this.WHITE[0];
	}
	public IntWritable get_WHITE_RENT() {
		return this.WHITE[1];
	}

	public IntWritable get_BLACK_OWN() {
		return this.BLACK[0];
	}
	public IntWritable get_BLACK_RENT() {
		return this.BLACK[1];
	}
	
	public IntWritable get_NATIV_OWN() {
		return this.NATIV[0];
	}
	public IntWritable get_NATIV_RENT() {
		return this.NATIV[1];
	}

	public IntWritable get_ASIAN_OWN() {
		return this.ASIAN[0];
	}
	public IntWritable get_ASIAN_RENT() {
		return this.ASIAN[1];
	}
	
	public IntWritable get_OTHER_OWN() {
		return this.OTHER[0];
	}
	public IntWritable get_OTHER_RENT() {
		return this.OTHER[1];
	}

	public void set_WHITE_OWN(String _POP) {
		this.WHITE[0] = new IntWritable(Integer.parseInt(_POP));
	}
	public void set_WHITE_RENT(String _POP) {
		this.WHITE[1] = new IntWritable(Integer.parseInt(_POP));
	}
	
	public void set_BLACK_OWN(String _POP) {
		this.BLACK[0] = new IntWritable(Integer.parseInt(_POP));
	}
	public void set_BLACK_RENT(String _POP) {
		this.BLACK[1] = new IntWritable(Integer.parseInt(_POP));
	}
	
	public void set_NATIV_OWN(String _POP) {
		this.NATIV[0] = new IntWritable(Integer.parseInt(_POP));
	}
	public void set_NATIV_RENT(String _POP) {
		this.NATIV[1] = new IntWritable(Integer.parseInt(_POP));
	}
	
	public void set_ASIAN_OWN(String _POP) {
		this.ASIAN[0] = new IntWritable(Integer.parseInt(_POP));
	}
	public void set_ASIAN_RENT(String _POP) {
		this.ASIAN[1] = new IntWritable(Integer.parseInt(_POP));
	}
	
	public void set_OTHER_OWN(String _POP) {
		this.OTHER[0] = new IntWritable(Integer.parseInt(_POP));
	}
	public void set_OTHER_RENT(String _POP) {
		this.OTHER[1] = new IntWritable(Integer.parseInt(_POP));
	}

}
