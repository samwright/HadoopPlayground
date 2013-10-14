package io.github.samwright.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * The famous MapReduce word count example for Hadoop.
 */
public class SimpleTool extends Configured implements Tool {

    private final Class mapper, combiner, reducer;
    private final String toolName;

    public SimpleTool(String toolName, Class mapper, Class combiner, Class reducer) {
        this.mapper = mapper;
        this.combiner = combiner;
        this.reducer = reducer;
        this.toolName = toolName;
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: "+toolName+" <in> <out>");
            ToolRunner.printGenericCommandUsage(System.err);
            return 1;
        }
        
        Job job = new Job(getConf(), toolName);
        job.setJarByClass(getClass());
        
        job.setMapperClass(mapper);
        job.setCombinerClass(combiner);
        job.setReducerClass(reducer);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    }
}
