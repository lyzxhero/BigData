package com.lyzx.spark.streaming

import java.{lang, util}

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.ConsumerStrategy

class Assign  extends ConsumerStrategy{

  override def executorKafkaParams: util.Map[String, Object] = ???

  override def onStart(currentOffsets: util.Map[TopicPartition, lang.Long]):Consumer[Nothing, Nothing] = ???
}
