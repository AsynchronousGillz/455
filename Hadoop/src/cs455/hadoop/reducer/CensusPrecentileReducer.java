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
	        		
	        		break;
	    		case 2:
	    			
	        		break;
        	}
        }
        context.write(key, data.toText());
    }
}
