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
        long day7 = 1541520000000L;
        long day8 = 1541606400000L;

        BufferedReader br = new BufferedReader(new FileReader("/Users/xiang/Downloads/var/x2.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/part-00008",true));

        int i = 0;
        String line = null;
        while((line = br.readLine()) != null){
            String time = line.substring(8,21);

            try{
                long now = Long.parseLong(time);
                if(now >= day7 && now < day8){
                    bw.write(line);
                    bw.newLine();
                }
                i++;
            }catch (Exception e){
                System.err.println("=="+line);
            }
        }
        br.close();
        bw.close();
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