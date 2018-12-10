package com.lyzx.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.*;



public class T1 {
    private static final String ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
    private static final String ips2 = "192.168.21.32:9092";


    @Test
    public void test1() throws Exception{
        Properties props = new Properties();
        props.put("bootstrap.servers",ips);
        props.put("group.id","ff");
//        props.put("auto.offset.reset","earliest");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("front-event"));


        while(true){
            ConsumerRecords<String, String> records = consumer.poll(3000);
            for(ConsumerRecord<String, String> record : records){
                String line = record.value();
                String r1 = record.offset()+"======"+record.partition()+"===="+line;
                System.out.println(r1);
            }
        }


    }



    @Test
    public void autoManageOffset2(){
        Properties props = new Properties();
        props.put("bootstrap.servers",ips);
        props.put("group.id","group_autoManageOffset3");
        props.put("auto.offset.reset","earliest");
        props.put("enable.auto.commit","false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("yh1"));

        while(true){
            ConsumerRecords<String, String> records = consumer.poll(1000);

            //通过records获取这个集合中的数据属于那几个partition
            Set<TopicPartition> partitions = records.partitions();
            for(TopicPartition tp : partitions){

                //通过具体的partition把该partition中的数据拿出来消费
                List<ConsumerRecord<String, String>> partitionRecords = records.records(tp);
                for(ConsumerRecord r : partitionRecords){
                    System.out.println(r.offset()   +"     "+r.key()+"     "+r.value());
                }
                //获取新这个partition中的最后一条记录的offset并加1 那么这个位置就是下一次要提交的offset
                long newOffset = partitionRecords.get(partitionRecords.size() - 1).offset() + 1;
                consumer.commitSync(Collections.singletonMap(tp,new OffsetAndMetadata(newOffset)));
            }
        }
    }


}