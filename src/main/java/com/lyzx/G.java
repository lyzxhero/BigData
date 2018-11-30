package com.lyzx;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

public class G {

    @Test
    public void test1(){
//        DateTimeFormatter folder = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        DateTimeFormatter text = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
//        LocalDateTime t = LocalDateTime.now();
//
//        String dir = t.format(folder)+"/"+t.format(text);
//        System.out.println(dir);

        String s = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
        System.out.println(s);

        String s2 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        System.out.println(s2);
    }

    public static final String getLocalIp() throws Exception {
        String ipString = "";
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")) {
//                    return ip.getHostName();
                    return ip.getHostAddress();
                }
            }
        }
        return ipString;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(getLocalIp());
//        long timeStamp = 1541993382921L;
//        LocalDateTime yesterday = LocalDateTime.ofEpochSecond(timeStamp/1000,0, ZoneOffset.ofHours(8));
//        String v = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(v);
    }
}
