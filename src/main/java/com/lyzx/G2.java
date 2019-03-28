package com.lyzx;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class G2 {

    public static void main(String[] args) throws Exception {
//        String hHmmss = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_HHmmssSSS"));
//        int random = new Random().nextInt(5000);
//        System.out.println(hHmmss);
//        System.out.println(random);

        recoverData();
    }



    public static void f2() throws Exception {
        BufferedReader my = new BufferedReader(new FileReader("/Users/xiang/Downloads/second_name"));
        String myline = null;
        int i = 0;
        while(null != (myline = my.readLine())){
            i++;
            JSONObject o = new JSONObject();
            o.put("id",i);
            o.put("second_name",myline);

//            String v = "("+i+",'"+myline+"'),";
//           String v =  "INSERT OVERWRITE TABLE join_demo_table SELECT "+i+" as id, '"+myline+"' as second_name";
//            String v = i+" "+myline;
            System.out.println(o);
        }
        my.close();


    }



    public static void f1() throws Exception {
        BufferedReader my = new BufferedReader(new FileReader("/Users/xiang/Downloads/regist/20181214my"));
        BufferedReader he = new BufferedReader(new FileReader("/Users/xiang/Downloads/regist/20181214he"));

        String myline = null;
        Set<String> mySet = new HashSet<>(512);
        while(null != (myline = my.readLine())){
            mySet.add(myline.trim());
        }
        my.close();


        String heline = null;
        Set<String> heSet = new HashSet<>(512);
        while(null != (heline = he.readLine())){
            heSet.add(heline.trim());
        }
        he.close();

        System.out.println(mySet.size());
        System.out.println(heSet.size());

        heSet.removeAll(mySet);
        System.out.println(heSet);
    }


    public static void recoverData() throws Exception {
        //M107000031_2018-12-21_002309.log
        BufferedReader my = new BufferedReader(new FileReader("/Users/xiang/Downloads/M107000031_right_data_manual"));
//        BufferedWriter right = new BufferedWriter(new FileWriter("/Users/xiang/Downloads/M107000030_right_data_manual",true));
        String myline = null;

        while(null != (myline = my.readLine())){
            try{
                JSONObject o = JSONObject.parseObject(myline);
                String target = o.toString();
//                System.out.println("target:"+target);
//                right.write(o.toString());
//                right.newLine();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("====>"+myline);
            }
        }
        my.close();
//        right.close();
    }
}
