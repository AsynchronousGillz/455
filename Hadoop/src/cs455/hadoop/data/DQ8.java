package cs455.hadoop.data;

import java.util.Map;
import java.util.TreeMap;

import cs455.hadoop.util.CollectData;

public final class DQ8 {
	
	private TreeMap<String, double[]> map;
	//
	
	public DQ8() {
		this.map = new TreeMap<>();
	}
	
	public void add_Data(String state, int OLD_POP, int TOTAL_POP) {
		if (this.map.get(state) != null) {
			double[] list = this.map.get(state);
			list[0] += OLD_POP;
			list[1] += TOTAL_POP;
			this.map.put(state, list);
		} else {
			this.map.put(state, new double[] {OLD_POP, TOTAL_POP});
		}
	}

	public String toString() {
		Map<String, Double> avg = new TreeMap<>();
		for (Map.Entry<String, double[]> entry : map.entrySet()) {
			String key = entry.getKey();
			avg.put(key, entry.getValue()[0]/entry.getValue()[1]);
		}
		String state = "";
		Double value = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : avg.entrySet()) {
			if (entry.getValue() > value) {
				state = entry.getKey();
				value = entry.getValue();
			}
		}
		return CollectData.printValue("PRECENTAGE OF ELDERLY: "+state+" ", value);
	}
	
}
