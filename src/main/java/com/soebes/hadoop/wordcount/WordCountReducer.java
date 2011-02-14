package com.soebes.hadoop.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws InterruptedException, IOException {
        long summ = 0;
        for (LongWritable value : values) {
            summ += value.get();
        }
        context.write(key, new LongWritable(summ));
    }
}
