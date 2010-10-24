#!/bin/bash
mvn -DskipTest=true -Dmaven.test.skip=true clean package
hadoop fs -rmr  tapelogs/result.lst
hadoop fs -rmr /tmp/hadoop-km/mapred/staging
hadoop jar target/hadoop-test-0.1.0-SNAPSHOT.jar com.soebes.hadoop.logfiles.TapeLogFileRunner tapelogs/logfile-test.log tapelogs/result.lst
