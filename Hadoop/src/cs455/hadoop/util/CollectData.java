package cs455.hadoop.util;

import org.apache.hadoop.io.Text;

public final class CollectData {

	public static String getTotal(Text value, int start, int count, int size) {
		Integer TOTAL = 0;
		for (int i = 0; i < count; i++, start += size) {
			TOTAL += Integer.parseInt(value.toString().substring(start, (start+size+1)));
		}
		return TOTAL.toString();
	}
	
	public static String getValue(Text value, int start, int size) {
		return value.toString().substring(start, (start+size+1));
	}
	
}
