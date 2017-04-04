package cs455.hadoop.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cs455.hadoop.util.ReducePrecentileData;
import cs455.hadoop.util.WritablePrecentileData;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<own|rent> pairs.
 * Sums up on per state basis a breakdown of the percentage of residences that were rented vs. owned
 */
public final class CensusPrecentileReducer extends Reducer<Text, WritablePrecentileData, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<WritablePrecentileData> values, Context context) throws IOException, InterruptedException {
        ReducePrecentileData data = new ReducePrecentileData();
    	// calculate the total count
        for(WritablePrecentileData val : values){
        	switch (val.get_SEGMENT().get()) {
	    		case 1:
	        		data.Q7.add_Data(val.get_STATE(), val.Q7.get_Percentile());
	        		break;
	    		case 2:
	    			data.Q8.add_Data(val.get_STATE(), val.Q8.get_OLD_POP().get(), val.Q8.get_TOTAL_POP().get());
	        		break;
        	}
        }
        context.write(key, data.toText());
    }
}
