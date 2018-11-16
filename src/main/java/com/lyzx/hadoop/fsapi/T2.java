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
        long day7 = 1542297600000L;
//        long day8 = 1542038399000L;

        BufferedReader br = new BufferedReader(new FileReader("/Users/xiang/Downloads/part-00002"));
        BufferedWriter bw15 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/15",true));
        BufferedWriter bw16 = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/16",true));

        int i = 0;
        String line = null;
        while((line = br.readLine()) != null){
            String time = line.substring(8,21);
            try{
                long now = Long.parseLong(time);
                if(now <= day7){
                    bw15.write(line);
                    bw15.newLine();
                }else{
                    bw16.write(line);
                    bw16.newLine();
                }
                i++;
            }catch (Exception e){
                System.err.println("=="+line);
            }
        }
        br.close();
        bw15.close();
        bw16.close();
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