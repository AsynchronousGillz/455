package cs455.hadoop.data;

import java.util.Map;
import java.util.TreeMap;

import cs455.hadoop.util.CollectData;

public final class DQ6 {

	private Map<String, Integer> map;
	//
	
	public DQ6() {
		this.map = new TreeMap<>();
	}
	
	public void add_Median(int _MEDIAN) {
		String _VAL = CollectData.owner[_MEDIAN];
		if (this.map.get(_VAL) != null) {
			this.map.put(_VAL, this.map.get(_VAL) + 1);
		} else {
			this.map.put(_VAL, 1);
		}
	}
	
	public String toString() {
		int listTotal = 0;
		for(Map.Entry<String,Integer> entry : map.entrySet()) {
			listTotal += entry.getValue();
		}
		int valueIndex = listTotal / 2;
		int index = 0;
		for(Map.Entry<String,Integer> entry : map.entrySet()) {
			valueIndex -= entry.getValue();
			if (valueIndex <= 0) {
				break;
			}
			index++;
		}
		return CollectData.printText("OWNER MEDIAN COST:", CollectData.owner[index]);
	}
	
}
