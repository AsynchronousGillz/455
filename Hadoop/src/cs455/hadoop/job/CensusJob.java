package cs455.hadoop.job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import cs455.hadoop.mapper.CensusPrecentileMapper;
import cs455.hadoop.mapper.CensusStateMapper;
import cs455.hadoop.reducer.CensusPrecentileReducer;
import cs455.hadoop.reducer.CensusStateReducer;
import cs455.hadoop.util.WritablePrecentileData;
import cs455.hadoop.util.WritableStateData;

import java.io.IOException;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public final class CensusJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job state = Job.getInstance(conf, "ganvana_StateJob");
            state.setJarByClass(CensusJob.class);
            // Mapper
            state.setMapperClass(CensusStateMapper.class);
            // Reducer
            state.setReducerClass(CensusStateReducer.class);
            
            state.setMapOutputKeyClass(Text.class);
            state.setMapOutputValueClass(WritableStateData.class);
            
            state.setOutputKeyClass(Text.class);
            state.setOutputValueClass(Text.class);
            
            FileInputFormat.addInputPath(state, new Path(args[0]));
            FileOutputFormat.setOutputPath(state, new Path(args[1]));
            
            state.waitForCompletion(true);
            
            Job percentile = Job.getInstance(conf, "ganvana_PercentileJob");
            percentile.setJarByClass(CensusJob.class);
            // Mapper
            percentile.setMapperClass(CensusPrecentileMapper.class);
            // Reducer
            percentile.setReducerClass(CensusPrecentileReducer.class);
            
            percentile.setMapOutputKeyClass(Text.class);
            percentile.setMapOutputValueClass(WritablePrecentileData.class);
            
            percentile.setOutputKeyClass(Text.class);
            percentile.setOutputValueClass(Text.class);
            
            FileInputFormat.addInputPath(percentile, new Path(args[0]));
            FileOutputFormat.setOutputPath(percentile, new Path(args[2]));
            
            // Block until the job is completed.
            System.exit(percentile.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

    }
}
