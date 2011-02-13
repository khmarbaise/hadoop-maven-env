package com.soebes.hadoop.logfiles;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TapeLogFileMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    public enum TapeLogFile {
        FILES,
        DIRECTORIES,
        LINKS,
        VOLUMNHEADER,
        EMPTY,
        BYTES,
    }

    public void map(LongWritable key, Text value, Context context)
        throws InterruptedException, IOException {

        String strLine = value.toString();
        String[] columns = strLine.split("[ ]+");

        if (strLine.startsWith("tar: /dev/nst0")) {
            context.getCounter(TapeLogFile.EMPTY).increment(1);
            return;
        }

        if (columns[2].startsWith("-")) {
            //We only count files, but no directories or links etc.
            long sizeInBytes = Long.parseLong(columns[4]);
            context.getCounter(TapeLogFile.BYTES).increment(sizeInBytes);

            context.getCounter(TapeLogFile.FILES).increment(1);
        }

        if (columns[2].startsWith("d")) {
            context.getCounter(TapeLogFile.DIRECTORIES).increment(1);
        }

        if (columns[2].startsWith("l")) {
            context.getCounter(TapeLogFile.LINKS).increment(1);
        }
        if (columns[2].startsWith("V")) {
            context.getCounter(TapeLogFile.VOLUMNHEADER).increment(1);
        }
    }
}
