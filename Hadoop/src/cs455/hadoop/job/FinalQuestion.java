package cs455.hadoop.job;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs455.hadoop.util.CollectData;

public class FinalQuestion {

	
	public static double getValue(String line, String regex) {
		return Double.parseDouble(line.substring(23).split(regex)[0]);
	}
	
	public static void main(String[] args) {
		List<Double> nineFive = new ArrayList<>();
		String stateValue = "";
		double oldValue = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			for (String line = ""; (line = br.readLine()) != null;) {
				String tmpState = "";
				double tmpOld = 0;
				if (line.charAt(0) != ' ')
					tmpState = line.substring(0, 2);
				if (line.contains("AVG ROOM"))
					nineFive.add(getValue(line, " "));
				if (line.contains("PERCENTAGE OF 85+"))
					tmpOld = getValue(line, "%");
				if (tmpOld > oldValue) {
					stateValue = tmpState;
					oldValue = tmpOld;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int index = (int) (nineFive.size() * .95);
		System.out.println("Q7: "+CollectData.room[index]);
		System.out.println("Q8: "+stateValue+" => "+oldValue+"%");
	}

}
