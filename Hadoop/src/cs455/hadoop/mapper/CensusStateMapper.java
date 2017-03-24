package cs455.hadoop.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cs455.hadoop.util.CollectData;
import cs455.hadoop.util.WritableData;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Mapper: Reads line by line, gets the state. Emit <Text, {@link WritableData}> pairs.
 */
public final class CensusStateMapper extends Mapper<LongWritable, Text, Text, WritableData> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

    	if (Integer.parseInt(value.toString().substring(10, 13)) == 100) {
    		StringTokenizer state = new StringTokenizer(value.toString().substring(8, 10));
    		WritableData data = new WritableData();
    		data.set_SEGMENT(CollectData.getValue(value, 24, 4));
    		boolean write = true;
    		switch (data.get_SEGMENT().get()) {
	    		case 1:
	    			data.set_POPULATION(CollectData.getTotal(value, 300, 1, 9));
	    			data.Q2.set_M(CollectData.getTotal(value, 4422, 1, 9)); 
	    			data.Q2.set_F(CollectData.getTotal(value, 4467, 1, 9));
	    			
	    			data.Q3.set_M_AGE_1(CollectData.getTotal(value, 3972, 1, 9));
	    			data.Q3.set_M_AGE_2(CollectData.getTotal(value, 3981, 5, 9)); 
	    			data.Q3.set_M_AGE_3(CollectData.getTotal(value, 4026, 2, 9));
	    			
	    			data.Q3.set_F_AGE_1(CollectData.getTotal(value, 4251, 1, 9));
	    			data.Q3.set_F_AGE_2(CollectData.getTotal(value, 4260, 5, 9)); 
	    			data.Q3.set_F_AGE_3(CollectData.getTotal(value, 4305, 2, 9));
	    	        break;
	    		case 2:
	    			data.Q1.set_R(CollectData.getTotal(value, 1803, 1, 9));
	    			data.Q1.set_O(CollectData.getTotal(value, 1812, 1, 9));
			        break;
	    		default:
	    			write = false;
    		} // switch
    		if (write)
    			context.write(new Text(state.nextToken()), data);
    	} // summary 100 
    }
}
