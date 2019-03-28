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


public class StructureStreamingDemo4 {
    private static final String ips = "172.16.124.70:9092,172.16.124.71:9092,172.16.124.72:9092";
    private static final String topicName1 = "joinDemo";
//    private static final String topicName2 = "joinDemo2";

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("app")
//                .master("local[6]")
                .enableHiveSupport()
                .getOrCreate();



        spark.sql("use jf_logs");
        Dataset<Row> result = spark.sql("SELECT id,second_name FROM join_demo_table");
        result.toDF()
                .foreach(item->{
                    String id = item.getAs("id");
                    String second_name = item.getAs("second_name");
                    System.out.println("id="+id+"  second_nam="+second_name);
                });
    }

}
