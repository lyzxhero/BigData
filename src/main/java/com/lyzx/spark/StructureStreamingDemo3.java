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

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;
import static org.apache.spark.sql.types.DataTypes.IntegerType;
import static org.apache.spark.sql.types.DataTypes.StringType;


public class StructureStreamingDemo3 {
    private static final String ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
    private static final String topicName1 = "joinDemo";
//    private static final String topicName2 = "joinDemo2";

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("app")
                .master("local[6]")
//                .enableHiveSupport()
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

//        d1.createOrReplaceGlobalTempView(topicName1);
        d1.createOrReplaceTempView(topicName1);

//        d1.foreach(row->{
//            System.out.println("=====>"+row.getAs("first_name"));
//        });


        StreamingQuery console =
                d1.writeStream()
                .format("console")
                .outputMode(OutputMode.Append())
                .start();

        try{
            console.awaitTermination();
        }catch(StreamingQueryException e) {
            e.printStackTrace();
        }


//
////        Dataset<Row> hive = spark.read().format("orc").load();
//
//        spark.sql("use jf_logs");
//        Dataset<Row> result = spark.sql("select * from "+topicName1+" a inner join join_demo_table b on a.id = b.id");
//
//
//        StreamingQuery console =
//                result
//                .writeStream()
//                .format("console")
//                .outputMode(OutputMode.Append())
//                .start();
//
//        try {
//            console.awaitTermination();
//        } catch (StreamingQueryException e) {
//            e.printStackTrace();
//        }


    }

}
