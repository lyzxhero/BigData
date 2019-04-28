package com.lyzx.hive.lineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author hero
 * <p>
 * 生成hive表的血缘图，以树状图存储并在前端使用树形展示
 */
public class HiveLineAgeGraphGenerator {
    public static void main(String[] args) {
        String dir = "/Users/xiang/Documents/code/data-collection/data-platform/src/test/resources/bi_report/";
        File fDir = new File(dir);
        File[] hql = fDir.listFiles();

        Map<String, Set<String>> inAndOutRelease = new HashMap<>();


        for (File item : hql) {
            Map<String, Set<String>> result = HiveLineAgeParser.parse(item);
            Set<String> in = result.get("in");
            Set<String> out = result.get("out");
            Iterator<String> itr = out.iterator();
            boolean b = itr.hasNext();
            String outTable = itr.next();
            inAndOutRelease.put(outTable, in);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode ditems = mapper.createArrayNode();
        ArrayNode themes = mapper.createArrayNode();

        TreeSet<String> theme = new TreeSet<>();

        int index = 1;
        for(Map.Entry<String, Set<String>> kv : inAndOutRelease.entrySet()) {
            String outTable = kv.getKey();

            ObjectNode item = mapper.createObjectNode();
            item.put("type","ditem");
            item.put("name",outTable);
            item.put("description","");
            item.put("ditem",index);
            item.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            item.put("slug",outTable);

            ArrayNode links = mapper.createArrayNode();

            Set<String> inTables = kv.getValue();
            Iterator<String> itr = inTables.iterator();
            while(itr.hasNext()){
                String link = itr.next();
                links.add(link);
                theme.add(link);
            }
            item.set("links",links);
            ditems.add(item);
            index++;
        }

        result.set("ditems",ditems);

        Iterator<String> itr = theme.iterator();
        while(itr.hasNext()){
            String link = itr.next();
            ObjectNode themItem = mapper.createObjectNode();
            themItem.put("type","theme");
            themItem.put("name",link);
            themItem.put("description","");
            themItem.put("slug",link);
            themes.add(themItem);
        }

        result.set("themes",themes);

        System.out.println("::::"+result);
    }
}
