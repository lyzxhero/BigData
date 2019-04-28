package com.lyzx.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lyzx.domain.Data;
import com.lyzx.domain.Data2;
import com.lyzx.domain.Data3;
import com.lyzx.domain.Views;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JackSonTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test1() {
        Data data = new Data();
        data.setAge(11);
        data.setName("liyaohui2");

        try {
            mapper.writeValue(new File("/Users/xiang/Downloads/data.json"), data);
            String s = mapper.writeValueAsString(data);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() {
        try {
            Data data = mapper.readValue(new File("/Users/xiang/Downloads/data.json"), Data.class);
            System.out.println(data);
            String s = "{\"name\":\"liyaohui2\",\"age\":11}";

            Data data1 = mapper.readValue(s, Data.class);
            System.out.println(data1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        Data2 data2 = new Data2();
        data2.setAge(22);
        data2.setName("肖露");

        Data data = new Data();
        data.setName("李耀辉");
        data.setAge(24);

        List<Data> datas = new ArrayList<>();
        datas.add(data);
        data2.setDatas(datas);

        try {
            String s = mapper.writeValueAsString(data2);
            System.out.println(s);

            String s1 =
                    mapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(data2);

            System.out.println(s1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        Data3 data3 = new Data3();
        data3.setAge(33);
        data3.setName("耀辉");
        data3.setExtra("x1");
        data3.setHeight(99);
        data3.setWeight(8);

        try {
            String s = mapper.writerWithView(Views.Normal.class)
//                    .withDefaultPrettyPrinter()
                    .writeValueAsString(data3);

            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        String k = "{\"name\":\"耀辉\",\"age\":33,\"weight\":8.0,\"height\":99.0}";

        try {
            Object o = mapper.readValue(k, new TypeReference<Data3>() {});
            System.out.println(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test6() {
        File file = new File("/Users/xiang/Downloads/data.json");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            JsonNode node = mapper.readTree(in);

            long id = node.path("id").asLong();
            System.out.println(id);

            JsonNode contact = node.path("contact");
            if (contact.isArray()) {
                for (JsonNode x : contact) {
                    System.out.println("::" + x.path("type"));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test7() {
        File file = new File("/Users/xiang/Downloads/data.json");
        try {
            JsonNode jsonNode = mapper.readTree(file);
            String s = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonNode);

            System.out.println(s);

            ((ObjectNode) jsonNode).put("lyzx_hero", "k1");


            ObjectNode node = mapper.createObjectNode();
            node.put("k1", "v1");
            node.put("k2", 99);

            ((ObjectNode) jsonNode).set("new_object", node);


            ArrayNode arrayNode = mapper.createArrayNode();
            ObjectNode node1 = mapper.createObjectNode();
            node1.put("new_k1", "new_v1");
            node1.put("new_k2", "new_v2");
            arrayNode.add(node1);

            ObjectNode node2 = mapper.createObjectNode();
            node2.put("new_k1_2", "new_v1");
            node2.put("new_k2_2", "new_v2");
            arrayNode.add(node2);

            ((ObjectNode) jsonNode).set("new_arr", arrayNode);


            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("love", "肖露");

            ArrayNode contact = ((ArrayNode) jsonNode.path("contact")).add(objectNode);
            System.out.println("contact=" + contact);


            String s1 = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonNode);


            System.out.println(s1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
