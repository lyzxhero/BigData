package com.lyzx.hadoop.fsapi;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sun.deploy.trace.Trace.flush;


public class T1 {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/yyyyMMdd");
    private static final Map<String,PrintWriter> writers = new HashMap<>();
    private static final Map<String,FSDataOutputStream> appenders = new HashMap<>();
    private static final FileSystem fs = configureFileSystem();



    public static FileSystem configureFileSystem(){
        FileSystem fileSystem = null;
        try {
            Configuration conf = new Configuration();
            conf.setBoolean("dfs.support.append", true);
            fileSystem = FileSystem.get(new URI("hdfs://cdh-master1:8020"),conf,"root");
        }catch(Exception ex){
            System.out.println("Error occurred while configuring FileSystem");
        }
        return fileSystem;
    }

    public String appendToFile(PrintWriter writer,String content) throws IOException {
        writer.append(content);
        writer.println();
        return "Success";
    }


    public void closeFileSystem(FileSystem fileSystem){
        try{
            fileSystem.close();
        }catch(IOException ex){
            System.out.println("----------Could not close the FileSystem----------");
        }
    }
    

    public static void main(String[] args) throws IOException{
        long start = System.currentTimeMillis();
        T1 example = new T1();
//        FileSystem fs = example.configureFileSystem();

//        fs.createNewFile(new Path("/front-event/2018/11/07/20181107"));
//        fs.close();

        BufferedReader br = new BufferedReader(new FileReader("/Users/xiang/Downloads/part-00008"));

        int i = 0;
        String line = null;
        while((line = br.readLine()) != null){
            String time = line.substring(8,21);
            String filePath = convert(time);
            PrintWriter writer = createPrintWriter(filePath);
            example.appendToFile(writer,line);
            i++;
        }
        br.close();
        System.out.println("line Count="+i);

        for(String key : writers.keySet()){
            PrintWriter w = writers.get(key);
            w.flush();
            FSDataOutputStream fds = appenders.get(key);
            fds.hflush();
            w.close();
            fds.close();
        }

        long end = System.currentTimeMillis();
        System.out.println("=====>"+(end-start));
        example.closeFileSystem(fs);
    }



    public static String convert(String timeStamp){
        long v = Long.parseLong(timeStamp);
        Date d = new Date(v);
        return df.format(d);
    }


    public static PrintWriter createPrintWriter(String filePath) throws IOException {
        if(writers.containsKey(filePath)){
            return writers.get(filePath);
        }else{
            System.out.println("创建writer:"+filePath);
            Path destPath = new Path("/front-event/"+filePath);
            FSDataOutputStream fs_append = fs.append(destPath);
            appenders.put(filePath,fs_append);
            PrintWriter writer = new PrintWriter(fs_append);
            writers.put(filePath,writer);
            return writer;
        }
    }


//    public static Path getPath(){
//        LocalDateTime now = LocalDateTime.now();
//        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
//        String month = now.format(DateTimeFormatter.ofPattern("MM"));
//        String day = now.format(DateTimeFormatter.ofPattern("dd"));
//
//        String v = File.separator;
//        String dir = "/front-event/"+v+year+v+month+v+day+v+year+month+day;
//        return new Path(dir);
//    }

}