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

    public void map(
            LongWritable key,
            Text value,
            Context context)
            throws InterruptedException, IOException {

        // Assume we have a single line from the log file.
		/*
        block 0: V--------- 0/0               0 1970-01-01 01:00:00 20100904040001--Volume Header--
        block 1: drwxr-xr-x root/root         0 2010-08-28 15:09:12 ./
        block 2: drwxr-xr-x root/root         0 2006-05-06 15:53:51 ./bin/
        block 3: -rwxr-xr-x root/root     41064 2005-03-19 21:28:24 ./bin/dd
        block 85: -rwxr-xr-x root/root     58648 2005-03-19 21:28:24 ./bin/cp
        block 201: -rwxr-xr-x root/root     36276 2005-03-19 21:28:24 ./bin/df
        block 273: -rwxr-xr-x root/root     43780 2005-03-19 20:05:44 ./bin/ed
        block 360: -rwxr-xr-x root/root     25152 2005-03-19 21:28:24 ./bin/ln
        block 411: -rwxr-xr-x root/root     78136 2005-03-19 21:28:24 ./bin/ls
        block 565: -rwxr-xr-x root/root     65840 2005-03-19 21:28:24 ./bin/mv
         */

        String strLine = value.toString();
        String[] columns = strLine.split("[ ]+");

        if (strLine.startsWith("tar: /dev/nst0")) {
//			tar: /dev/nst0: Cannot open: Input/output error
//			tar: Error is not recoverable: exiting now
            context.getCounter(TapeLogFile.EMPTY).increment(1);
            return;
        }

        if (columns[2].startsWith("-")) {
            //We only count files, but no directories or links etc.
            long sizeInBytes = Long.parseLong(columns[4]);
            context.getCounter(TapeLogFile.BYTES).increment(sizeInBytes);

            context.getCounter(TapeLogFile.FILES).increment(1);
        }

//		if (columns[2].startsWith("d")) {
//			context.getCounter(TapeLogFile.DIRECTORIES).increment(1);
//			context.write(new Text("directory"), new Text("Test"));
//		}

        if (columns[2].startsWith("l")) {
            context.getCounter(TapeLogFile.LINKS).increment(1);
        }
        if (columns[2].startsWith("V")) {
            context.getCounter(TapeLogFile.VOLUMNHEADER).increment(1);
        }
    }
}
