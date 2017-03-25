package cs455.hadoop.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cs455.hadoop.util.ReduceData;
import cs455.hadoop.util.WritableData;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<own|rent> pairs.
 * Sums up on per state basis a breakdown of the percentage of residences that were rented vs. owned
 */
public final class CensusStateReducer extends Reducer<Text, WritableData, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<WritableData> values, Context context) throws IOException, InterruptedException {
        ReduceData data = new ReduceData();
    	// calculate the total count
        for(WritableData val : values){
        	switch (val.get_SEGMENT().get()) {
	    		case 1:
	        		data.Q2.add_F_MARRIED(val.Q2.get_F_MARRIED().get());
	        		data.Q2.add_M_MARRIED(val.Q2.get_M_MARRIED().get());
	        		data.Q2.add_POP(val.get_POPULATION().get());
	        		
	        		data.Q3.add_F_1_AGE(val.Q3.get_F_AGE_1().get());
	        		data.Q3.add_F_2_AGE(val.Q3.get_F_AGE_2().get());
	        		data.Q3.add_F_3_AGE(val.Q3.get_F_AGE_3().get());
	        		
	        		data.Q3.add_M_1_AGE(val.Q3.get_M_AGE_1().get());
	        		data.Q3.add_M_2_AGE(val.Q3.get_M_AGE_2().get());
	        		data.Q3.add_M_3_AGE(val.Q3.get_M_AGE_3().get());
	        		data.Q3.add_POP(val.get_POPULATION().get());
	        		break;
	    		case 2:
	        		data.Q1.add_OWN(val.Q1.get_OWN().get());
	        		data.Q1.add_RENT(val.Q1.get_RENT().get());
	        		break;
        	}
        }
        context.write(key, data.toText());
    }
}
