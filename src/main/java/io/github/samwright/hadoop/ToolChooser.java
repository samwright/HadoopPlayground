package io.github.samwright.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.ToolRunner;

/**
 * User: Sam Wright
 * Date: 26/02/2013
 * Time: 17:36
 */
public class ToolChooser {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: WordCount <in> <out> OR OddEvenSummer <in> <out>");
            System.exit(1);
        }

        String[] toolArgs = new String[2];
        toolArgs[0] = args[1];
        toolArgs[1] = args[2];

        SimpleTool tool = null;

        if (args[0] == "WordCount")
            tool = new SimpleTool("WordCount", TokenizingMapper.class, IntSumReducer.class, IntSumReducer.class);
        else if (args[0] == "OddEvenSummer")
            tool = new SimpleTool("OddEvenSummer", OddEvenMapper.class, IntSumReducer.class, IntSumReducer.class);


        System.out.format(" ------ chosen tool is %s ------ ", args[0]);


        int res = ToolRunner.run(new Configuration(), tool, toolArgs);

        System.exit(res);
    }
}
