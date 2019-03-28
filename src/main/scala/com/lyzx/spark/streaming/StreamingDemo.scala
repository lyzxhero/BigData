package com.lyzx.spark.streaming

import org.apache.spark.sql._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.streaming.StreamingQueryException
import org.apache.spark.sql.types.MetadataBuilder
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes.IntegerType
import org.apache.spark.sql.types.DataTypes.StringType


class StreamingDemo {

}


object StreamingDemo{
  val ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092"
  val topicName1 = "joinDemo"
  val topicName2 = "joinDemo2"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
                    .builder()
                    .appName("app")
                    .master("local[6]")
                    .getOrCreate();


            val df1 = spark
                    .readStream
                    .format("kafka")
                    .option("kafka.bootstrap.servers",ips)
                    .option("startingOffsets","earliest")
                    .option("subscribe",topicName1)
                    .load();


            val b = new MetadataBuilder()
            val schema1 = StructType(Array( StructField("id",IntegerType, true,b.build()),StructField("first_name",StringType, true,b.build())));


            df1.select(col("key").cast("string"),from_json(col("value").cast("string"),schema1))
               .createOrReplaceTempView(topicName1)



                    val df2 = spark
                    .readStream
                    .format("kafka")
                    .option("kafka.bootstrap.servers",ips)
                    .option("startingOffsets", "earliest")
                    .option("subscribe",topicName2)
                    .load()

            val schema2 = StructType(Array(StructField("id",IntegerType, true,b.build()),StructField("second_name",StringType, true,b.build())))
                df2.select(col("key").cast("string"),from_json(col("value").cast("string"),schema2))
                    .createOrReplaceTempView(topicName2)



            val result = spark.sql("select * from "+topicName1+" a inner join "+topicName2+" b on a.id = b.id");


            val console =
                    result
//                    .mapPartitions(itr)->{
//
//    //                    while(itr.hasNext()){
//    //                        Row row = itr.next();
//    //                        row.getAs("");
//    //                    }
//                        return Collections.EMPTY_LIST.iterator();
//                     },Encoders.STRING())
                    .writeStream
                    .format("console")
                    .outputMode(OutputMode.Append())
                    .start();

            try {
                console.awaitTermination();
            } catch {
              case e : StreamingQueryException =>
                e.printStackTrace();
            }
  }
}