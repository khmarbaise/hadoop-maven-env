#!/bin/bash
mvn -DskipTest=true -Dmaven.test.skip=true clean package
hadoop fs -rmr  result-wordcount.lst
hadoop fs -rmr /tmp/hadoop-km/mapred/staging
hadoop jar target/hadoop-test-0.1.0-SNAPSHOT.jar com.soebes.hadoop.wordcount.WordCountRunner /user/km/wordcount/words.txt result-wordcount.lst
