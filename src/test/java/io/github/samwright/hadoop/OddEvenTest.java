package io.github.samwright.hadoop;

import io.github.samwright.hadoop.OddEvenMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * User: Sam Wright
 * Date: 25/02/2013
 * Time: 23:08
 */
public class OddEvenTest {
    private MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
    private ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    private MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

    @Before
    public void setUp() {
        OddEvenMapper mapper = new OddEvenMapper();
        IntSumReducer<Text> reducer = new IntSumReducer<Text>();

        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
    }

    @Test
    public void testMapper() {
        mapDriver.withInput(new LongWritable(), new Text("1;2;3;4"));
        mapDriver.withOutput(new Text("odd"), new IntWritable(1));
        mapDriver.withOutput(new Text("odd"), new IntWritable(3));
        mapDriver.withOutput(new Text("even"), new IntWritable(2));
        mapDriver.withOutput(new Text("even"), new IntWritable(4));

        mapDriver.runTest(false);
    }

    @Test
    public void testReducer() {
        IntWritable once = new IntWritable(1);

        reduceDriver.withInput(new Text("foo"), Arrays.asList(once, once, once));
        reduceDriver.withOutput(new Text("foo"), new IntWritable(3));

        reduceDriver.runTest();
    }

    @Test
    public void testMapReduce() {
        mapReduceDriver.withInput(new LongWritable(), new Text("1;2;3;4"));

        mapReduceDriver.withOutput(new Text("odd"), new IntWritable(4));
        mapReduceDriver.withOutput(new Text("even"), new IntWritable(6));

        mapReduceDriver.runTest(false);     // 'false' implies i don't care about ordering of output
    }

}

