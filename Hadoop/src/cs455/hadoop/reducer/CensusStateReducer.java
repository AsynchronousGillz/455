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
        	if (val.get_SEGMENT().get() == 1) {
        		data.add_Q2_F_MARRIED(val.get_F_MARRIED().get());
        		data.add_Q2_M_MARRIED(val.get_M_MARRIED().get());
        	}
        	if (val.get_SEGMENT().get() == 2) {
        		data.add_Q1_OWN(val.get_OWN().get());
        		data.add_Q1_RENT(val.get_RENT().get());
        	}
        }
        context.write(key, new Text(data.toString()));
    }
}
