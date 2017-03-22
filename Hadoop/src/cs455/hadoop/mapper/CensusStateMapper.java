package cs455.hadoop.mapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cs455.hadoop.util.WritableData;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Mapper: Reads line by line, gets the state. Emit <state, own|rent> pairs.
 */
public final class CensusStateMapper extends Mapper<LongWritable, Text, Text, WritableData> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

    	if (Integer.parseInt(value.toString().substring(10, 13)) == 100) {
    		StringTokenizer state = new StringTokenizer(value.toString().substring(8, 10));
    		WritableData data = new WritableData();
    		data.set_SEGMENT(Integer.parseInt(value.toString().substring(24, 28)));
    		boolean write = true;
    		switch (data.get_SEGMENT().get()) {
	    		case 1:
	    			data.set_M_MARRIED(Integer.parseInt(value.toString().substring(4422, 4432)));
	    			data.set_F_MARRIED(Integer.parseInt(value.toString().substring(4467, 4476)));
	    	        break;
	    		case 2:
	    			data.set_OWN(Integer.parseInt(value.toString().substring(1803, 1812)));
	    			data.set_RENT(Integer.parseInt(value.toString().substring(1812, 1821)));
			        break;
	    		default:
	    			write = false;
    		} // switch
    		if (write)
    			context.write(new Text(state.nextToken()), data);
    	} // summary 100 
    }
}
