package cs455.hadoop.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cs455.hadoop.util.CollectData;
import cs455.hadoop.util.WritableStateData;

import java.io.IOException;

/**
 * Mapper: Reads line by line, gets the state. Emit <Text, {@link WritableStateData}> pairs.
 */
public final class CensusStateMapper extends Mapper<LongWritable, Text, Text, WritableStateData> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		if (Integer.parseInt(value.toString().substring(10, 13)) == 100) {
			Text state = new Text(value.toString().substring(8, 10));
			WritableStateData data = new WritableStateData();
			data.set_SEGMENT(CollectData.getTotal(value, 24, 1, 4));
			boolean write = true;
			switch (data.get_SEGMENT().get()) {
				case 1:
					data.set_POPULATION(CollectData.getTotal(value, 300, 1, 9));
					data.Q2.set_M(CollectData.getTotal(value, 4422, 1, 9)); 
					data.Q2.set_F(CollectData.getTotal(value, 4467, 1, 9));

					data.Q3.set_M_AGE_1(CollectData.getTotal(value, 3864, 13, 9));
					data.Q3.set_M_AGE_2(CollectData.getTotal(value, 3981, 5, 9)); 
					data.Q3.set_M_AGE_3(CollectData.getTotal(value, 4026, 2, 9));
					data.Q3.set_M_TOTAL(CollectData.getTotal(value, 4044, 11, 9));

					data.Q3.set_F_AGE_1(CollectData.getTotal(value, 4143, 13, 9));
					data.Q3.set_F_AGE_2(CollectData.getTotal(value, 4260, 5, 9)); 
					data.Q3.set_F_AGE_3(CollectData.getTotal(value, 4305, 2, 9));
					data.Q3.set_F_TOTAL(CollectData.getTotal(value, 4323, 11, 9));

					data.Q7.set_Percentile(value, 2388, 9);
					break;
				case 2:
					data.set_POPULATION(CollectData.getTotal(value, 300, 1, 9));
					data.Q1.set_O(CollectData.getTotal(value, 1803, 1, 9));
					data.Q1.set_R(CollectData.getTotal(value, 1812, 1, 9));

					data.Q4.set_URBAN(CollectData.getTotal(value, 1821, 2, 9));
					data.Q4.set_RURAL(CollectData.getTotal(value, 1839, 1, 9));
					data.Q4.set_EXTRA(CollectData.getTotal(value, 1849, 1, 9));

					data.Q5.set_Median(value, 2928, 9);

					data.Q6.set_Median(value, 3450, 9);

					data.Q8.set_OLD_POP(CollectData.getTotal(value, 1065, 1, 9));
					break;
				default:
					write = false;
			} // switch
			if (write)
				context.write(state, data);
		} // summary 100 
	}
}
