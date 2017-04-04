package cs455.hadoop.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cs455.hadoop.util.CollectData;
import cs455.hadoop.util.WritablePrecentileData;

import java.io.IOException;

/**
 * Mapper: Reads line by line, gets the state. Emit <Text, {@link WritableStateData}> pairs.
 */
public final class CensusPrecentileMapper extends Mapper<LongWritable, Text, Text, WritablePrecentileData> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

    	if (Integer.parseInt(value.toString().substring(10, 13)) == 100) {
    		String state = value.toString().substring(8, 10);
    		WritablePrecentileData data = new WritablePrecentileData(state);
    		data.set_SEGMENT(CollectData.getTotal(value, 24, 1, 4));
    		switch (data.get_SEGMENT().get()) {
	    		case 1:
	    			data.Q7.set_Percentile(value, 2388, 9);
	    			context.write(new Text("7"), data);
	    	        break;
	    		case 2:
	    			data.Q8.set_OLD_POP(CollectData.getTotal(value, 1065, 1, 9));
	    			data.Q8.set_TOTAL_POP(CollectData.getTotal(value, 300, 1, 9));
	    			context.write(new Text("8"), data);
			        break;
    		} // switch
    	} // summary 100 
    }
}
