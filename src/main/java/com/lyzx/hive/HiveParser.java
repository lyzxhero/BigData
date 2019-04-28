package com.lyzx.hive;

import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;

import java.util.ArrayList;

/**
 *
 * @author hero
 *
 */
public class HiveParser {

    public static void main(String[] args) throws ParseException {
        ParseDriver driver = new ParseDriver();

//        String sql = "INSERT OVERWRITE TABLE liuxiaowen.lxw3 SELECT a.url FROM liuxiaowen.lxw1 a join liuxiaowen.lxw2 b ON (a.url = b.domain)";
        String sql = "select a.id,a.name,b.age,c.user1 from (select * from db1.user1 where gender='1' ) a left join (select * from db1.user2 where gender='2' ) b on a.id = b.id";
        ASTNode astNode = driver.parse(sql);

        System.out.println(astNode.getToken());
        System.out.println(astNode.getChildCount());


        ArrayList<Node> children = astNode.getChildren();
        for(Node n : children){
            System.out.println(":::"+n+"-------"+n.getName()+","+n.getChildren());
        }
        System.out.println("=============");

//        while ((astNode.getToken() == null) && (astNode.getChildCount() > 0)) {
//            astNode = (ASTNode) astNode.getChild(0);
//        }
//        System.out.println(astNode);
    }
}
