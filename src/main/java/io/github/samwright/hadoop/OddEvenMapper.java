package io.github.samwright.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * User: Sam Wright
 * Date: 26/02/2013
 * Time: 17:48
 */
public class OddEvenMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static Text odd = new Text("odd"), even = new Text("even");

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        final String[] strings = value.toString().split(";");

        for (String string : strings) {
            IntWritable number = new IntWritable(Integer.parseInt(string));

            if (number.get() % 2 == 0)
                context.write(even, number);
            else
                context.write(odd, number);
        }

    }
}
