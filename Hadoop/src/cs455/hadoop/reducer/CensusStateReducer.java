package cs455.hadoop.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cs455.hadoop.util.ReduceStateData;
import cs455.hadoop.util.WritableStateData;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<own|rent> pairs.
 * Sums up on per state basis a breakdown of the percentage of residences that were rented vs. owned
 */
public final class CensusStateReducer extends Reducer<Text, WritableStateData, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<WritableStateData> values, Context context) throws IOException, InterruptedException {
		ReduceStateData data = new ReduceStateData();
		// calculate the total count
		for(WritableStateData val : values){
			switch (val.get_SEGMENT().get()) {
				case 1:
					data.Q2.add_F_MARRIED(val.Q2.get_F_MARRIED().get());
					data.Q2.add_M_MARRIED(val.Q2.get_M_MARRIED().get());
					data.Q2.add_POP(val.get_POPULATION().get());

					data.Q3.add_F_1_AGE(val.Q3.get_F_AGE_1().get());
					data.Q3.add_F_2_AGE(val.Q3.get_F_AGE_2().get());
					data.Q3.add_F_3_AGE(val.Q3.get_F_AGE_3().get());
					data.Q3.add_F_POP(val.Q3.get_F_TOTAL().get());

					data.Q3.add_M_1_AGE(val.Q3.get_M_AGE_1().get());
					data.Q3.add_M_2_AGE(val.Q3.get_M_AGE_2().get());
					data.Q3.add_M_3_AGE(val.Q3.get_M_AGE_3().get());
					data.Q3.add_M_POP(val.Q3.get_M_TOTAL().get());

					data.Q8.add_OLD_POP(val.Q8.get_OLD_POP().get());
					data.Q8.add_POP(val.get_POPULATION().get());
					break;
				case 2:
					data.Q1.add_OWN(val.Q1.get_OWN().get());
					data.Q1.add_RENT(val.Q1.get_RENT().get());

					data.Q4.add_RURAL(val.Q4.get_RURAL().get());
					data.Q4.add_URBAN(val.Q4.get_URBAN().get());
					data.Q4.add_EXTRA(val.Q4.get_EXTRA().get());

					data.Q5.add_Median(val.Q5.get_Median());

					data.Q6.add_Median(val.Q6.get_Median());
					
					data.Q7.add_Data(val.Q7.get_Percentile());
					
					data.Q9.add_ASIAN_OWN(val.Q9.get_ASIAN_OWN().get());
					data.Q9.add_ASIAN_RENT(val.Q9.get_ASIAN_RENT().get());
					data.Q9.add_BLACK_OWN(val.Q9.get_BLACK_OWN().get());
					data.Q9.add_BLACK_RENT(val.Q9.get_BLACK_RENT().get());
					data.Q9.add_NATIV_OWN(val.Q9.get_NATIV_OWN().get());
					data.Q9.add_NATIV_RENT(val.Q9.get_NATIV_RENT().get());
					data.Q9.add_OTHER_OWN(val.Q9.get_OTHER_OWN().get());
					data.Q9.add_NATIV_RENT(val.Q9.get_OTHER_RENT().get());
					data.Q9.add_WHITE_OWN(val.Q9.get_WHITE_OWN().get());
					data.Q9.add_WHITE_RENT(val.Q9.get_WHITE_RENT().get());
					break;
			}
		}
		context.write(key, data.toText());
	}
}
