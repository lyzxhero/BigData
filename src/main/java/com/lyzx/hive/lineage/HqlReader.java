package com.lyzx.hive.lineage;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author  hero
 *
 * 读取hql文件，读取的hql文件是清洗之后的完整的查询语句
 * 去除了诸如:
 *  1、set hive.exec.dynamic.partition.mode=nonstrict;
 *  2、------------
 *  3、select '-----start-----';
 *  4、空行
 *  等行信息
 */
public class HqlReader {
    private static final HqlReader hqlReader = new HqlReader();
    private static final Map<String,Object> TEMPLATE_PARAM = new LinkedHashMap<>();
    private final  Pattern COMMON_HIVE_PARAM;
    private final  Pattern SQL_COMMENT;
    private final  Pattern SQL_TEST;
    private String ts;

    private HqlReader(){
        COMMON_HIVE_PARAM = Pattern.compile("\\$\\{([\\w:])+\\}");
        SQL_COMMENT = Pattern.compile("\\s+--\\s?.+");
        SQL_TEST = Pattern.compile("\\s+from\\s+");
        ts = null;
    }

    static List<String> readHql(File hqlFile){
        hqlReader.init();

        List<String> hql = new ArrayList<>();
        StringBuilder sqlBlock = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(hqlFile.getAbsoluteFile()))) {
            String line;

            while (null != (line = reader.readLine())) {
                line = line.trim();
                boolean save = hqlReader.filter(line);
                if (!save) {
                    continue;
                }

                boolean case_start_drop = line.length() >= 4 && line.substring(0, 4).equalsIgnoreCase("drop");
                boolean case_start_insert = line.length() >= 6 && line.substring(0, 6).equalsIgnoreCase("insert");

                if (case_start_drop || case_start_insert) {
                    if (!"".equals(sqlBlock.toString())) {
                        hql.add(sqlBlock.toString());
                    }

                    sqlBlock = new StringBuilder();
                    if (case_start_drop) {
                        continue;
                    }
                }

                line = hqlReader.clear(line);
                sqlBlock.append(line).append(" ");
            }
            hql.add(sqlBlock.toString());
            return hqlReader.convert(hql);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

        hqlReader.clear();
        return hql;
    }


    /**
     *
     * @param sqlList List中的每一项都是一个完整的query 一个sqlList 包含一个hql所有的查询语句
     * @return  返回经过字符串模板处理后的hql
     * @throws IOException 普通的IO意向
     * @throws TemplateException 在字符串模板替换变量失败时抛出，一般情况下是${varName}的varName中有特殊字符
     */
    public List<String> convert(List<String> sqlList) throws IOException, TemplateException {
        for(int i=0;i<sqlList.size();i++){
            String sqlBlock = sqlList.get(i);
            String TEMPLATE_VERSION = "2.3.23";
            Template template = new Template("index"+i,sqlBlock , new Configuration(new Version(TEMPLATE_VERSION)));
            StringWriter result = new StringWriter();
            template.process(TEMPLATE_PARAM, result);
            sqlList.set(i,result.toString());
        }
        return sqlList;
    }

    /**
     * 选择需要过滤的行
     * @param line hql文件的一行 并不一定是完整的一句query
     * @return false表示需要过滤，true表示需要保留
     *
     * case1:过滤空行
     * case2:过滤hive参数设置信息
     * case3:过滤注释信息
     * case4:过滤调试信息 select '----start-----' ;
     */
    private boolean filter(String line){
        line = line.toLowerCase();
        boolean case1 = "".equals(line);
        boolean case2 = line.startsWith("set");
        boolean case3 = line.startsWith("--");
        boolean case4 = line.startsWith("select") && !SQL_TEST.matcher(line).find() && line.endsWith(";");

        //如果java有个lazy关键字该多好啊! 不影响性能先不管
        return !case1 && !case2 && !case3 && !case4;
    }


    /**
     *
     * @param line hql文件的一行 并不一定是完整的一句query
     * @return 返回处理后的hql  eg: where apply_date > ${hiveconf:last_week_date}   ==> where apply_date > ${last_week_date}
     */
    private String clear(String line){
        //去掉每一句话后面的注释信息
        Matcher matchComment = SQL_COMMENT.matcher(line);
        if(matchComment.find()){
            line = line.replaceAll(SQL_COMMENT.pattern(),"");
        }

        Matcher matcher = COMMON_HIVE_PARAM.matcher(line);
        while(matcher.find()){
            String target = matcher.group();
            String coreParam = target.substring(2,target.length()-1);

            //处理${ts}
            String tsPreFix = "ts";
            if(tsPreFix.equalsIgnoreCase(coreParam)){
                TEMPLATE_PARAM.put(coreParam, ts);
                continue;
            }

            //处理${hive_schema_bi_report}
            String db_pre_fix = "hive_schema_";
            if(coreParam.startsWith(db_pre_fix)){
                String dbName = coreParam.substring(coreParam.indexOf(db_pre_fix) + db_pre_fix.length());
                TEMPLATE_PARAM.put(coreParam,dbName);
                continue;
            }

            //处理${hiveconf:param}
            String hiveParamPreFix = "hiveconf:";
            if(coreParam.startsWith(hiveParamPreFix)){
                String replacement = target.substring(target.indexOf(":")+1,target.length()-1);
                TEMPLATE_PARAM.put(replacement,replacement);
                line = line.replace(target,"${"+replacement+"}");
                continue;
            }

            //处理${job_name}
            String jobNamePreFix = "job_name";
            if(jobNamePreFix.equalsIgnoreCase(coreParam)){
                TEMPLATE_PARAM.put(jobNamePreFix,"");
            }
        }
        if(line.endsWith(";")){
            line = line.substring(0,line.length()-1);
        }
        return  line;
    }

    private void init(){
        if(null == ts){
            ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        }
    }

    private void clear(){
        ts = null;
        TEMPLATE_PARAM.clear();

    }

}
