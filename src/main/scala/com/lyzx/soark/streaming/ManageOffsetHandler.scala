package com.lyzx.soark.streaming

import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer


class ManageOffsetHandler {

}

object ManageOffsetHandler{

  def main(args: Array[String]): Unit = {

       val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "localhost:9092,anotherhost:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "use_a_separate_group_id_for_each_stream",
            "auto.offset.reset" -> "latest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
          )

    val conf = new SparkConf()
                  .setAppName("test123")
                  .setMaster("local[5]")

    val ssc = new StreamingContext(conf,Seconds(1))
    val fromOffsets = new mutable.HashMap[TopicPartition,Long]()


    val line = KafkaUtils.createDirectStream[String, String](ssc,
          PreferConsistent,
          Assign[String, String](fromOffsets.keys.toList, kafkaParams, fromOffsets)
        )

    line.foreachRDD(rdd=>{
      val offsetRange:Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      val vv:OffsetRange = null;

      rdd.foreachPartition(itr=>{

        itr.foreach(item=>print(item.value()))


      })
    })

  }
}
