package com.soebes.hadoop.logfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TapeLogFileReducerTest {

    private Reducer<Text, LongWritable, Text, LongWritable> reducer;
    private ReduceDriver<Text, LongWritable, Text, LongWritable> driver;

    @BeforeClass
    public void setUp() {
        reducer = new TapeLogFileReducer();
        driver = new ReduceDriver<Text, LongWritable, Text, LongWritable>(reducer);
    }

    @Test
    public void firstTest() throws IOException {
        LongWritable[] parList = {new LongWritable(1), new LongWritable(2)};
        driver.withInput(new Text("Bytes"), Arrays.asList(parList));
        List<Pair<Text, LongWritable>> result = driver.run();

        for (Pair<Text, LongWritable> pair : result) {
            System.out.println("1Text=" + pair.getFirst() + " Long:" + pair.getSecond());
        }

        driver.withOutput(new Text("Bytes"), new LongWritable(3));
        driver.runTest();
    }
}
