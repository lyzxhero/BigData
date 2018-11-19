package com.lyzx.kafka.producer;



import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerTest {
        private final String ips1 = "192.168.21.32:9092";
        private final String ips2 = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";

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

        Producer<String,String> producer = new KafkaProducer<>(props);
        for (int i = 700; i <800; i++){
            Future<RecordMetadata> f = producer.send(new ProducerRecord<>("front-event", Integer.toString(i), Integer.toString(i)));
            System.out.println(f.get());
        }
        producer.close();
    }

    @Test
    public void test1(){
        String s = LocalDateTime.now().plusDays(-6).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        System.out.println(s);

    }
}
