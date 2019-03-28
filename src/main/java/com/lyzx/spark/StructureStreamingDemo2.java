package com.lyzx.spark;


import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.MetadataBuilder;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import java.util.Collections;
import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.types.DataTypes.IntegerType;
import static org.apache.spark.sql.types.DataTypes.StringType;


public class StructureStreamingDemo2 {
    private static final String ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
    private static final String topicName1 = "joinDemo";
    private static final String topicName2 = "joinDemo2";

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("app")
//                .master("local[6]")
                .getOrCreate();


        Dataset<Row> df1 = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers",ips)
                .option("startingOffsets","earliest")
                .option("subscribe",topicName1)
                .option("failOnDataLoss","false")
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
                .selectExpr("v.id","v.first_name as first_name");

        d1.createOrReplaceTempView(topicName1);
//        d1.createOrReplaceGlobalTempView(topicName1);


        Dataset<Row> df2 = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers",ips)
                .option("startingOffsets", "earliest")
                .option("subscribe",topicName2)
                .option("failOnDataLoss","false")
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
                .selectExpr("v.id","v.second_name as second_name");


        d2.createOrReplaceTempView(topicName2);
//        d2.createOrReplaceGlobalTempView(topicName2);

        Dataset<Row> result = spark.sql("select * from "+topicName1+" a inner join "+topicName2+" b on a.id = b.id");


        StreamingQuery console =
                result

//                .mapPartitions((org.apache.spark.api.java.function.MapPartitionsFunction<Iterable<Row>,Iterable<String>> itr)->{
////                    while(itr.hasNext()){
////                        Row row = itr.next();
////                        row.getAs("");
////                    }
//
//                    return Collections.EMPTY_LIST.iterator();
//                 },Encoders.STRING())

                .writeStream()
                .format("console")
                .outputMode(OutputMode.Append())
                .start();

        try {
            console.awaitTermination();
        } catch (StreamingQueryException e) {
            e.printStackTrace();
        }


    }

}
