package com.lyzx.kafka.producer;



import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerTest {
        private final String ips1 = "metal3:9092";
        private final String ips2 = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
        private final String topicName = "front-event-local";
        private final String topicName2 = "joinDemo2";


    @Test
    public void producer1() throws ExecutionException, InterruptedException {

        Properties props = new Properties();
        props.put("bootstrap.servers",ips1);
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size",16384);
        props.put("linger.ms",1);
        props.put("buffer.memory",33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

//        String[] arr1 = "郑 罗 宋 谢 唐 韩 曹 许 邓 萧 冯 曾 程 蔡 彭 潘 袁 于 董 余 苏 叶 吕 魏 蒋 田 杜 丁 沈 姜 范 江 傅 钟 卢 汪 戴 崔 任 陆 廖 姚 方 金 邱 夏 谭 韦 贾 邹 石 熊 孟 秦 阎 薛 侯 雷 白 龙 殷 郝 孔 邵 史 毛 常 万 顾 赖 武 康 贺 严 尹 钱 施 牛 洪 龚 李 王 张 刘 陈 杨 黄 赵 周 吴 徐 孙 朱 马 胡 郭 林 何 高 梁".trim().split(" ");
//        List<String> firstName_top100 = Arrays.asList(arr1);


        Producer<String,String> producer = new KafkaProducer<>(props);
        for(int i = 1000; i <2000; i++){
            JSONObject o = new JSONObject();
            o.put("id",String.valueOf(i));
            o.put("first_name", UUID.randomUUID().toString());

            Future<RecordMetadata> f = producer.send(new ProducerRecord<>(topicName, Integer.toString(i),o.toString()));
            System.out.println(f.get());
        }
        producer.close();
    }

    @Test
    public void producer2() throws ExecutionException, InterruptedException, IOException {

        Properties props = new Properties();
        props.put("bootstrap.servers",ips1);
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size",16384);
        props.put("linger.ms",1);
        props.put("buffer.memory",33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        Producer<String,String> producer = new KafkaProducer<>(props);
        BufferedReader my = new BufferedReader(new FileReader("/Users/xiang/Downloads/second_name"));
        String myline = null;
        int i = 0;
        while(null != (myline = my.readLine())){
            i++;
            JSONObject o = new JSONObject();
            o.put("id",i);
            o.put("second_name",myline);

            Future<RecordMetadata> f = producer.send(new ProducerRecord<>(topicName2, Integer.toString(i),o.toString()));
            System.out.println(f.get());
        }
        my.close();

    }

    @Test
    public void test1(){
        String s = LocalDateTime.now().plusDays(-6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        System.out.println(s);

    }
}
