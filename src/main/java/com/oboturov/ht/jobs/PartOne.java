package com.oboturov.ht.jobs;

import com.oboturov.ht.Nuplet;
import com.oboturov.ht.Tweet;
import com.oboturov.ht.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.ChainMapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @author aoboturov
 */
public class PartOne extends Configured implements Tool {

    private static final boolean TWEETS_COUNTER_KEY = true;

    private static class CountMeasureMap extends MapReduceBase implements Mapper<User, Nuplet, BooleanWritable, LongWritable> {
        @Override
        public void map(final User user, final Nuplet nuplet, final OutputCollector<BooleanWritable, LongWritable> outputCollector, final Reporter reporter) throws IOException {
            outputCollector.collect(new BooleanWritable(TWEETS_COUNTER_KEY), new LongWritable(1L));
        }
    }

    @Override
    public int run(final String[] args) throws Exception {
        final Configuration config = getConf();

        final JobConf jobConf = new JobConf(config, PartOne.class);
        jobConf.setJobName("part-one");

        jobConf.setOutputKeyClass(NullWritable.class);
        jobConf.setOutputValueClass(Nuplet.class);

        jobConf.setInputFormat(TextInputFormat.class);
        jobConf.setOutputFormat(TextOutputFormat.class);

        // Extract tweets
//        ChainMapper.addMapper(
//                jobConf,
//                TweetsReader.Map.class,
//                LongWritable.class,
//                Text.class,
//                LongWritable.class,
//                Tweet.class,
//                false,
//                new JobConf(false)
//        );
//        // Map tweets to user-tweet pair.
//        ChainMapper.addMapper(
//                jobConf,
//                NupletCreator.Map.class,
//                LongWritable.class,
//                Tweet.class,
//                User.class,
//                Nuplet.class,
//                true,
//                new JobConf(false)
//        );
//        // Unshorten URLs.
//        ChainMapper.addMapper(
//                jobConf,
//                URIResolver.Map.class,
//                User.class,
//                Nuplet.class,
//                User.class,
//                Nuplet.class,
//                true,
//                new JobConf(false)
//        );
//        // Detect tweets language
//        ChainMapper.addMapper(
//                jobConf,
//                LanguageIdentificationWithLangGuess.LanguageIdentificationMap.class,
//                User.class,
//                Nuplet.class,
//                User.class,
//                Nuplet.class,
//                true,
//                new JobConf(false)
//        );
//        // Count number of non-english tweets
//        ChainMapper.addMapper(
//                jobConf,
//                PhraseTokenizer.PhraseTokenizerMap.class,
//                User.class,
//                Nuplet.class,
//                NullWritable.class,
//                Nuplet.class,
//                true,
//                new JobConf(false)
//        );

        FileInputFormat.setInputPaths(jobConf, new Path(args[args.length - 2]));
        FileOutputFormat.setOutputPath(jobConf, new Path(args[args.length - 1]));

        JobClient.runJob(jobConf);

        return 0;
    }

    public static void main(final String args[]) throws Exception {
        for (String arg: args) {
            System.out.println(arg);
        }

        // Let ToolRunner handle generic command-line options
        int res = ToolRunner.run(new PartOne(), args);

        System.exit(res);
    }
}
