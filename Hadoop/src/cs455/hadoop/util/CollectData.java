package cs455.hadoop.util;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;

public final class CollectData {
	
	public static String printPrecent(String CAT, Double VALUE) {
		return new String(String.format("   %1$-25s %2$-3.2f", CAT, VALUE*100)+"%\n");
	}
	
	public static String printValue(String CAT, Double VALUE) {
		return new String(String.format("   %1$-25s %2$-3.2f", CAT, VALUE)+"\n");
	}

	public static String getTotal(Text value, int start, int count, int size) {
		Integer TOTAL = 0;
		for (int i = 0; i < count; i++, start += size) {
			TOTAL += Integer.parseInt(value.toString().substring(start, (start+size)));
		}
		return TOTAL.toString();
	}
	
	public static String getMedian(Text value, int start, int count, int size) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < count; i++, start += size) {
			list.add(Integer.parseInt(value.toString().substring(start, (start+size))));
		}
		Collections.sort(list);
		int half = list.size()/2;
		if (list.size() %2 == 0)
			return list.get(half).toString();
		else
			return new Integer((list.get((half) + list.get(1+half)) / 2)).toString();
	}
	
}
