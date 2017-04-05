package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

public final class DQ8 {

	//
	private double OLD_POP;
	private double TOTAL_POP;

	public DQ8() {
		this.OLD_POP = 0;
		this.TOTAL_POP = 0;
	}

	public void add_OLD_POP(int _OLD_POP) {
		this.OLD_POP += _OLD_POP;
	}

	public void add_POP(int _POP) {
		this.TOTAL_POP += _POP;
	}

	public String toString() {
		return CollectData.printValue("PERCENTAGE OF 85+:", (this.OLD_POP / this.TOTAL_POP)*100);
	}

}
