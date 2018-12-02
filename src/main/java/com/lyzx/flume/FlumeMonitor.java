package com.lyzx.flume;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class FlumeMonitor {

    public static void main(String[] args) {
        //Creates CloseableHttpClient instance with default configuration.
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://192.168.56.104:34545/metrics");

        try{
            CloseableHttpResponse execute = httpCilent.execute(httpGet);
            StatusLine statusLine = execute.getStatusLine();

            int code = statusLine.getStatusCode();
            String reasonPhrase = statusLine.getReasonPhrase();
            String content = EntityUtils.toString(execute.getEntity());
            System.out.println(content);
            System.out.println("code="+code);
            System.out.println("reasonPhrase="+reasonPhrase);

            execute.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}