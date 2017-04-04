package cs455.hadoop.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cs455.hadoop.util.CollectData;

public final class DQ7 {
	
	private TreeMap<String, int[]> map;
	//
	private TreeMap<String, Integer> count;
	
	public DQ7() {
		this.map = new TreeMap<>();
		this.count = new TreeMap<>();
	}
	
	public void add_Data(String state, int[] _MEDIAN) {
		int index = 0;
		if (this.map.get(state) != null) {
			int[] list = this.map.get(state);
			for (int i : _MEDIAN)
				list[index++] += i;
			this.map.put(state, list);
		} else {
			this.map.put(state, _MEDIAN);
		}
		
		if (this.map.get(state) != null)
			this.count.put(state, this.count.get(state) + 1);
		else
			this.count.put(state, 1);
	}

	public String toString() {
		int index = 0;
		List<Double> avg = new ArrayList<>();
		for (Map.Entry<String, int[]> entry : map.entrySet()) {
			String key = entry.getKey();
			double info = this.count.get(key);
			double total = 0; 
			for (int i : entry.getValue())
				total += i;
			avg.add(total/info);
		}
		Collections.sort(avg);
		index = (int) (avg.size() * .95);
		return CollectData.printText("95th PRECENTILE:", CollectData.room[index]);
	}
	
}
