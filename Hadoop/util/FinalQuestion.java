import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class FinalQuestion {

	
	public static double getValue(String line, String regex) {
		return Double.parseDouble(line.substring(29).split(regex)[0].trim());
	}
	
	public static void main(String[] args) {
		List<Double> nineFive = new ArrayList<>();
		String stateValue = "";
		double oldValue = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			String tmpState = "";
			double tmpOld = 0;
			for (String line = ""; (line = br.readLine()) != null;) {
				if (line.equals("") == true)
					continue;
				if (line.charAt(0) != ' ')
					tmpState = line.substring(0, 2);
				if (line.contains("95TH PERCENTILE"))
					nineFive.add(getValue(line, " "));
				if (line.contains("PERCENTAGE OF 85+"))
					tmpOld = getValue(line, "%");
				if (tmpOld > oldValue) {
					stateValue = new String(tmpState);
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
		Collections.sort(nineFive);
		int index = (int) (nineFive.size() * .95);
		System.out.println("Q7: "+(int)(nineFive.get(index).doubleValue())+" rooms");
		System.out.println("Q8: "+stateValue+" => "+oldValue+"%");
	}

}
