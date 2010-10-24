package com.soebes.hadoop.logfiles;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.soebes.hadoop.logfiles.TapeLogFileMapper.TapeLogFile;

public class TapeLogFileMapperTest {

    private Mapper<LongWritable, Text, Text, LongWritable> mapper;
    private MapDriver<LongWritable, Text, Text, LongWritable> driver;

    @BeforeClass
    public void setUp() {
        mapper = new TapeLogFileMapper();
        driver = new MapDriver<LongWritable, Text, Text, LongWritable>(mapper);
    }

    @Test
    public void firstTest() throws IOException {
        driver.withInput(new LongWritable(1234), new Text("block 3: -rwxr-xr-x root/root     41064 2005-03-19 21:28:24 ./bin/dd"));
        List<Pair<Text, LongWritable>> result = driver.run();
        for (Pair<Text, LongWritable> pair : result) {
            System.out.println("1Text=" + pair.getFirst() + " Long:" + pair.getSecond());
        }
        driver.withOutput(new Text("Bytes"), new LongWritable(41064));
    }

    @Test
    public void secondTest() throws IOException {
        driver.withInput(new LongWritable(1235), new Text("block 3: -rwxr-xr-x root/root     1 2005-03-19 21:28:24 ./bin/dd"));
        List<Pair<Text, LongWritable>> result = driver.run();
        for (Pair<Text, LongWritable> pair : result) {
            System.out.println("2Text=" + pair.getFirst() + " Long:" + pair.getSecond());
        }
        driver.withOutput(new Text("Bytes"), new LongWritable(1));
    }

    @Test
    public void thirdTest() throws IOException {
        driver.withInput(new LongWritable(1235), new Text("tar: /dev/nst0: Cannot open: Input/output error"));

        List<Pair<Text, LongWritable>> result = driver.run();
        for (Pair<Text, LongWritable> pair : result) {
            System.out.println("3Text=" + pair.getFirst() + " Long:" + pair.getSecond());
        }
        Counter emptyCounter = driver.getCounters().findCounter(TapeLogFile.EMPTY);
        System.out.println("Counter: " + emptyCounter.getValue());
        driver.withOutput(new Text("Bytes"), new LongWritable(1));
    }
}
