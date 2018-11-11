package com.lyzx.hadoop.fsapi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hero.li
 *
 */
public class GeneralApiTest {
    private static FileSystem fs;
    static{
        Configuration conf = new Configuration();
        try {
            fs = FileSystem.get(new URI("hdfs://192.168.56.104:9000"),conf,"root");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void test1() throws URISyntaxException, IOException, InterruptedException {
        boolean b = fs.mkdirs(new Path("/front-event/today"));
        System.out.printf("b="+b);
    }


    /**
     * 若存在就级联删除并返回true，否则返回false
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        boolean b = fs.deleteOnExit(new Path("/front-event"));
        System.out.printf("b="+b);
    }


    @Test
    public void test3() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        String s = String.join(",", list);
        System.out.printf("s="+s);
    }
}