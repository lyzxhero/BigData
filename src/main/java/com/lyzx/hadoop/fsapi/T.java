package com.lyzx.hadoop.fsapi;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class T {
    /**
     * To configure the file system as per the Hadoop Configuration
     * @return hadoop file system instance
     */
    public FileSystem configureFileSystem() {
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

    /**
     * appends content to hdfs file and also hflush the content to all the data nodes buffer
     * for availability.
     * @param content
     * @return status
     * @throws IOException
     */
    public String appendToFile(PrintWriter writer,String content) throws IOException {
        writer.append(content);
        writer.println();
        return "Success";
    }

    /**
     * To close the opened file system
     * @param fileSystem
     */
    public void closeFileSystem(FileSystem fileSystem){
        try{
            fileSystem.close();
        }catch(IOException ex){
            System.out.println("----------Could not close the FileSystem----------");
        }
    }


    public static void main(String[] args) throws IOException{
        long start = System.currentTimeMillis();
        T example = new T();
        FileSystem fs = example.configureFileSystem();


        String hdfsFilePath = "/lyzx/1.txt";
        BufferedReader br = new BufferedReader(new FileReader("D:\\test.txt"));

        Path destPath = new Path(hdfsFilePath);
        FSDataOutputStream fs_append = fs.append(destPath);
        PrintWriter writer = new PrintWriter(fs_append);
        String line = null;
        while((line = br.readLine()) != null){
            example.appendToFile(writer,line);
        }

        writer.flush();
        fs_append.hflush();
        writer.close();
        fs_append.close();
//
//        long end = System.currentTimeMillis();
//        System.out.println("=====>"+(end-start));
//        example.closeFileSystem(fs);

    }

    public Path getPath(){
        LocalDateTime now = LocalDateTime.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        String day = now.format(DateTimeFormatter.ofPattern("dd"));

        String v = File.separator;
        String dir = "/front-event/"+v+year+v+month+v+day+v+year+month+day;
        return new Path(dir);
    }

    @Test
    public void test1() throws Exception {
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://cdh-master1:8020"),conf);
        Path dir = getPath();

        FSDataOutputStream append = fileSystem.append(dir);
        PrintWriter w = new PrintWriter(append);

        for(int i=0;i<100;i++){
            w.append("==="+i);
            w.println();
        }

        w.flush();
        append.hflush();
        w.close();
        append.close();
    }

}