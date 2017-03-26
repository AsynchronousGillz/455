package cs455.hadoop.util;

import java.util.ArrayList;

import org.apache.hadoop.io.Text;

public final class CollectData {

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
		if (list.size() %2 == 0)
			return list.get(list.size()/2).toString();
		else
			return new Integer(list.get((list.size()/2) / list.get(1+list.size()/2)/2)).toString();
	}
	
}
