package cs455.hadoop.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;

public final class CollectData {
	
	public static String printPrecent(String CAT, Double VALUE) {
		return new String(String.format("   %1$-25s %2$-5.2f", CAT, VALUE*100)+"%\n");
	}
	
	public static String printValue(String CAT, Double VALUE) {
		return new String(String.format("   %1$-25s %2$-5.2f", CAT, VALUE)+"\n");
	}
	
	public static String printText(String CAT, String VALUE) {
		return new String(String.format("   %1$-25s %2$-7s", CAT, VALUE)+"\n");
	}

	public static String getTotal(Text value, int start, int count, int size) {
		Integer TOTAL = 0;
		for (int i = 0; i < count; i++, start += size) {
			TOTAL += Integer.parseInt(value.toString().substring(start, (start+size)));
		}
		return TOTAL.toString();
	}

	public static String[] owner = {
		"Less than $15,000", "$15,000 - $19,999", "$20,000 - $24,999", 
		"$25,000 - $29,999", "$30,000 - $34,999", "$35,000 - $39,999",
		"$40,000 - $44,999", "$45,000 - $49,999", "$50,000 - $59,999",
		"$60,000 - $74,999", "$75,000 - $99,999", "$100,000 - $124,999", 
		"$125,000 - $149,999", "$150,000 - $174,999", "$175,000 - $199,999",
		"$200,000 - $249,999", "$250,000 - $299,999", "$300,000 - $399,999",
		"$400,000 - $499,999", "$500,000 or more"
	};

	public static String[] rent = {
		"Less than $100", "$100 to $149", "$150 to $199", "$200 to $249",	
		"$250 to $299", "$300 to $349", "$350 to $399", "$400 to $449",
		"$450 to $499",	"$500 to $549", "$550 to $599", "$600 to $649",
		"$650 to $699", "$700 to $749", "$750 to $999", "$1000 or more", 
		"No cash rent"
	};
	
	public static String getMedian(Text value, int start, int count, int size) {
		int total = 0;
		Map<Integer, Integer> map = new TreeMap<>();
		for (int i = 0; i < count; i++, start += size) {
			map.put(new Integer(i), Integer.parseInt(value.toString().substring(start, (start + size))));
			total += map.get(i);
		}
		Integer index = 0;
		Integer indexValue = total / 2;
		for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
			indexValue -= entry.getValue();
			if (indexValue <= 0) {
				break;
			}
			index++;
		}
		return index.toString();
	}

}
