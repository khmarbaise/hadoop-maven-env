package com.soebes.hadoop.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    private LongWritable one = new LongWritable(1);
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context)
        throws InterruptedException, IOException {

        StringTokenizer token = new StringTokenizer(value.toString());
        while(token.hasMoreTokens()) {
            word.set(token.nextToken());
            context.write(word, one);
        }
    }
}
