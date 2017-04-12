package cs455.hadoop.data;

import cs455.hadoop.util.CollectData;

/**
 * Race owned vs rented 
 * Segment 2. 
 * Tenure by Race of Householder Owner occupied: White - 3150, Black - 3159, 
 * Native American - 3168, Asian or Pacific Islander - 3177, Other - 3187
 * 
 * Renter occupied: White - 3645, Black - 3654, Native American - 3663, Asian or
 * Pacific Islander - 3672, Other - 3681
 * 
 */

public final class DQ9 {

	// 
	private Double[] WHITE;
	private Double[] BLACK;
	private Double[] NATIV;
	private Double[] ASIAN;
	private Double[] OTHER;
	private Double TOTAL;
	
	public DQ9() {
		this.WHITE = new Double[2];
		this.BLACK = new Double[2];
		this.NATIV = new Double[2];
		this.ASIAN = new Double[2];
		this.OTHER = new Double[2];
		
		this.WHITE[0] = Double.valueOf(0);
		this.BLACK[0] = Double.valueOf(0);
		this.NATIV[0] = Double.valueOf(0);
		this.ASIAN[0] = Double.valueOf(0);
		this.OTHER[0] = Double.valueOf(0);
		
		this.WHITE[1] = Double.valueOf(0);
		this.BLACK[1] = Double.valueOf(0);
		this.NATIV[1] = Double.valueOf(0);
		this.ASIAN[1] = Double.valueOf(0);
		this.OTHER[1] = Double.valueOf(0);
		
		this.TOTAL = Double.valueOf(0);
	}
	
	public void add_WHITE_OWN(int _DATA) {
		this.WHITE[0] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_BLACK_OWN(int _DATA) {
		this.BLACK[0] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_NATIV_OWN(int _DATA) {
		this.NATIV[0] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_ASIAN_OWN(int _DATA) {
		this.ASIAN[0] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_OTHER_OWN(int _DATA) {
		this.OTHER[0] += _DATA;
		this.TOTAL += _DATA;
	}
	
	public void add_WHITE_RENT(int _DATA) {
		this.WHITE[1] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_BLACK_RENT(int _DATA) {
		this.BLACK[1] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_NATIV_RENT(int _DATA) {
		this.NATIV[1] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_ASIAN_RENT(int _DATA) {
		this.ASIAN[1] += _DATA;
		this.TOTAL += _DATA;
	}
	public void add_OTHER_RENT(int _DATA) {
		this.OTHER[1] += _DATA;
		this.TOTAL += _DATA;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(CollectData.printPrecent("WHITE OWN:",  (this.WHITE[0]/this.TOTAL)));
		sb.append(CollectData.printPrecent("BLACK OWN:",  (this.BLACK[0]/this.TOTAL)));
		sb.append(CollectData.printPrecent("NATIVE OWN:", (this.NATIV[0]/this.TOTAL)));
		sb.append(CollectData.printPrecent("ASIAN OWN:",  (this.ASIAN[0]/this.TOTAL)));
		sb.append(CollectData.printPrecent("OTHER OWN:",  (this.OTHER[0]/this.TOTAL)));
		sb.append("\n");
		sb.append(CollectData.printPrecent("WHITE RENT:",  (this.WHITE[1]/this.TOTAL)));
		sb.append(CollectData.printPrecent("BLACK RENT:",  (this.BLACK[1]/this.TOTAL)));
		sb.append(CollectData.printPrecent("NATIVE RENT:", (this.NATIV[1]/this.TOTAL)));
		sb.append(CollectData.printPrecent("ASIAN RENT:",  (this.ASIAN[1]/this.TOTAL)));
		sb.append(CollectData.printPrecent("OTHER RENT:",  (this.OTHER[1]/this.TOTAL)));
		return sb.toString(); 
	}
	
}
