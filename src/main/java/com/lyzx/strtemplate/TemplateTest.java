package com.lyzx.strtemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class TemplateTest {
    public static void main(String[] args) {
        try {
            Map<String,Object> map = new HashMap();

            //{${hive_schema_bi_report}=bi_report,
            // ${ts}=20190427105552,
            // ${hive_schema_bi_dm}=bi_dm,
            // ${last_week_today}=last_week_today}

            map.put("hive_schema_bi_report", "bi_report");
            map.put("point","last_week_today_123");
            map.put("p1","pppp1");

            Template template = new Template("strTpl", "insert overwrite table ${hive_schema_bi_report}.t_day_apply_pass_info_by_level  partition (ts='${ts}') select md5(apply_date)                                                                                     as id,                        apply_date                                                                                          as report_date,               nvl(first_risk_apply_amount,0)                                                                      as apply_amount_level1,       nvl(first_risk_approve_amount,0)                                                                    as approve_amount_level1,     nvl(last_risk_approve_amount,0)+nvl(last_risk_refuse_amount,0)                                      as apply_amount_level2,       nvl(last_risk_approve_amount,0)                                                                     as approve_amount_level2,     nvl(last_risk_approve_amount,0)                                                                     as apply_amount_level3,       nvl(asset_approve_in_last_risk_approve_amount,0)+nvl(asset_approve_in_last_risk_refuse_amount,0)    as approve_amount_level3,     current_timestamp()                                                                                 as create_time,               current_timestamp()                                                                                 as update_time,               'XUJIN'                                                                                             as creator_name,              'XUJIN'                                                                                             as updater_name               from ${hive_schema_bi_report}.temp_t_day_apply_pass_info_by_level_${ts}; \n", new Configuration(new Version("2.3.23")));
            StringWriter result = new StringWriter();
            template.process(map, result);
            System.out.println(result.toString());
            //您好张三，晚上好！您目前余额：10.16元，积分：10
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
