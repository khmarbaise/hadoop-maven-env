package com.soebes.hadoop.wordcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WordCountMapperTest {

    private Mapper<LongWritable, Text, Text, LongWritable> mapper;
    private MapDriver<LongWritable, Text, Text, LongWritable> driver;

    @BeforeClass
    public void setUp() {
        mapper = new WordCountMapper();
        driver = new MapDriver<LongWritable, Text, Text, LongWritable>(mapper);
    }

    @Test
    public void firstTest() throws IOException {
        driver.withInput(new LongWritable(1234), new Text("This is the old junky. There is the test."));
        List<Pair<Text, LongWritable>> result = driver.run();
        for (Pair<Text, LongWritable> pair : result) {
            System.out.println("1Text=" + pair.getFirst() + " Long:" + pair.getSecond());
        }
        List<Pair<Text, LongWritable>> expected = new ArrayList<Pair<Text,LongWritable>>();
        expected.add(new Pair<Text, LongWritable>(new Text("This"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("is"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("the"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("old"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("junky"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("There"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("is"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("the"), new LongWritable(1)));
        expected.add(new Pair<Text, LongWritable>(new Text("test"), new LongWritable(1)));

        Assert.assertEquals(expected, result);
    }

}
