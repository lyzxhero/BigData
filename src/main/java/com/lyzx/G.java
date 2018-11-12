package com.lyzx;

import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
