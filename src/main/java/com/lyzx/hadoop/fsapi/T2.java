package com.lyzx.hadoop.fsapi;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class T2 {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/yyyyMMdd");
    

    public static void main(String[] args) throws IOException{
        long start = System.currentTimeMillis();

//        long day03 = 1543766400000L;
        long day04 = 1543852800000L;
        long day05 = 1543939200000L;


        BufferedReader br = new BufferedReader(new FileReader("/Users/xiang/Downloads/originData"));
//        BufferedWriter bw05 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181205",true));
//        BufferedWriter bw04 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181204",true));


        BufferedWriter error = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/error",true));


        int i = 0;
        int realIndex = 0;
        String line = null;
        while((line = br.readLine()) != null){
            try{
                Long now = JSONObject.parseObject(line).getLong("time");
                if(now >= day04 && now < day05){
//                    bw04.write(line);
//                    bw04.newLine();
//                    realIndex++;
                }else if(now >= day05){
//                    bw05.write(line);
//                    bw05.newLine();
//                    realIndex++;
                }
                i++;
            }catch (Exception e){
                System.err.println("=="+line);
                error.write(line);
                error.newLine();
            }
        }
        br.close();

//        bw05.close();
//        bw04.close();

        error.close();



        System.out.println("总行数 Count="+i);
        System.out.println("有效行数 Count="+realIndex);

        long end = System.currentTimeMillis();
        System.out.println("=====>"+(end-start));
    }



    public static String convert(String timeStamp){
        long v = Long.parseLong(timeStamp);
        Date d = new Date(v);
        return df.format(d);
    }





}