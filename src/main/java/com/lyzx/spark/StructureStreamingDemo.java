package com.lyzx.spark;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.MetadataBuilder;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.types.DataTypes.IntegerType;
import static org.apache.spark.sql.types.DataTypes.StringType;


/**
 * @author hero.li
 * 最基础的流数据join
 */
public class StructureStreamingDemo {
    private static final String ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
    private static final String topicName1 = "joinDemo";
    private static final String topicName2 = "joinDemo2";

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("app")
                .master("local[6]")
                .getOrCreate();


        Dataset<Row> df1 = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers",ips)
                .option("startingOffsets","earliest")
                .option("subscribe",topicName1)
                .load();


        MetadataBuilder b = new MetadataBuilder();
        StructField[] fields = {
                new StructField("id",IntegerType, true,b.build()),
                new StructField("first_name",StringType, true,b.build())
        };

        StructType type = new StructType(fields);
        Dataset<Row> d1 = df1
                .withWatermark("timestamp","1 hours")
                .selectExpr("CAST(value AS STRING)")
                .select(from_json(col("value"),type).as("v"))
                .selectExpr("v.id as id1","v.first_name");


        Dataset<Row> df2 = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers",ips)
                .option("startingOffsets", "earliest")
                .option("subscribe",topicName2)
                .load();

        StructField[] fields2={
                new StructField("id",IntegerType, true,b.build()),
                new StructField("second_name",StringType, true,b.build())
        };

        StructType type2 = new StructType(fields2);
        Dataset<Row> d2 = df2
                .withWatermark("timestamp","1 hours")
                .selectExpr("CAST(value AS STRING)")
                .select(from_json(col("value"),type2).as("v"))
                .selectExpr("v.id as id2","v.second_name");

//        spark.sql("select * from ")
        StreamingQuery query = d1.join(d2,expr("id1 = id2"))
                .writeStream()
                .format("console")
                .outputMode(OutputMode.Append())
                .start();

        try{
            query.awaitTermination();
        }catch(StreamingQueryException e) {
            e.printStackTrace();
        }
    }

}
