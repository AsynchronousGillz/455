package cs455.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public final class WQ2 implements Writable {
	
	// 
	private IntWritable POPULATION;
	private IntWritable F_MARRIED;
	private IntWritable M_MARRIED;
	
	public WQ2() {
		this.POPULATION = this.F_MARRIED = this.M_MARRIED = new IntWritable(-1);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.POPULATION.readFields(arg0);
		this.F_MARRIED.readFields(arg0);
		this.M_MARRIED.readFields(arg0);
	}
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		this.POPULATION.write(arg0);
		this.F_MARRIED.write(arg0);
		this.M_MARRIED.write(arg0);
	}
	
	public IntWritable get_POPULATION() {
		return POPULATION;
	}
	
	public void set_POPULATION(String _POPULATION) {
		this.POPULATION = new IntWritable(Integer.parseInt(_POPULATION));
	}
	
	public IntWritable get_F_MARRIED() {
		return F_MARRIED;
	}

	public IntWritable get_M_MARRIED() {
		return M_MARRIED;
	}

	public void set_Q2(String M_MARRIED, String F_MARRIED) {
		this.M_MARRIED = new IntWritable(Integer.parseInt(M_MARRIED));
		this.F_MARRIED = new IntWritable(Integer.parseInt(F_MARRIED));
	}

}
