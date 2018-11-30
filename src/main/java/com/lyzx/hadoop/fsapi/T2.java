package com.lyzx.hadoop.fsapi;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class T2 {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/yyyyMMdd");









    

    public static void main(String[] args) throws IOException{
        long start = System.currentTimeMillis();

        long day15 = 1542211200000L;
        long day16 = 1542297600000L;
        long day17 = 1542384000000L;
        long day18 = 1542470400000L;
        long day19 = 1542556800000L;
        long day20 = 1542643200000L;

        BufferedReader br = new BufferedReader(new FileReader("/Users/xiang/Downloads/11/data-track/prod/front-event-2018-11-19-1.log"));
        BufferedWriter bw15 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181115",true));
        BufferedWriter bw16 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181116",true));
        BufferedWriter bw17 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181117",true));
        BufferedWriter bw18 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181118",true));
        BufferedWriter bw19 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/20181119",true));

        int i = 0;
        String line = null;
        while((line = br.readLine()) != null){
            line = line.split(" - ")[1];
            String time = line.substring(8,21);

            try{
                long now = Long.parseLong(time);
                if(now >= day15 && now <day16){
                    bw15.write(line);
                    bw15.newLine();
                }else if(now >= day16 && now < day17){
                    bw16.write(line);
                    bw16.newLine();
                }else if(now >= day17 && now < day18){
                    bw17.write(line);
                    bw17.newLine();
                }else if(now >= day18 && now < day19){
                    bw18.write(line);
                    bw18.newLine();
                }else if(now >= day19 && now < day20){
                    bw19.write(line);
                    bw19.newLine();
                }
                i++;
            }catch (Exception e){
                System.err.println("=="+line);
            }
        }

        br.close();
        bw15.close();
        bw16.close();
        bw17.close();
        bw18.close();
        bw19.close();

        System.out.println("line Count="+i);

        long end = System.currentTimeMillis();
        System.out.println("=====>"+(end-start));
    }



    public static String convert(String timeStamp){
        long v = Long.parseLong(timeStamp);
        Date d = new Date(v);
        return df.format(d);
    }





}