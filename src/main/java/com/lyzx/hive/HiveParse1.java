package com.lyzx.hive;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.hive.ql.lib.DefaultGraphWalker;
import org.apache.hadoop.hive.ql.lib.DefaultRuleDispatcher;
import org.apache.hadoop.hive.ql.lib.Dispatcher;
import org.apache.hadoop.hive.ql.lib.GraphWalker;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.lib.NodeProcessor;
import org.apache.hadoop.hive.ql.lib.NodeProcessorCtx;
import org.apache.hadoop.hive.ql.lib.Rule;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.BaseSemanticAnalyzer;
import org.apache.hadoop.hive.ql.parse.HiveParser;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.hadoop.hive.ql.parse.SemanticException;

/**
 * lxw的大数据田地 -- lxw1234.com
 * @author lxw1234
 *
 */
public class HiveParse1 implements NodeProcessor {
    private static final HiveParse1 parser = new HiveParse1();

    private HiveParse1(){}

    /**
     * Stores input tables in sql.
     */
    TreeSet inputTableList = new TreeSet();
    /**
     * Stores output tables in sql.
     */
    TreeSet OutputTableList = new TreeSet();

    /**
     *
     * @return java.util.TreeSet
     */
    public TreeSet getInputTableList() {
        return inputTableList;
    }

    /**
     * @return java.util.TreeSet
     */
    public TreeSet getOutputTableList() {
        return OutputTableList;
    }

    /**
     * Implements the process method for the NodeProcessor interface.
     */
    public Object process(Node nd, Stack stack, NodeProcessorCtx procCtx,Object... nodeOutputs) throws SemanticException {
        ASTNode pt = (ASTNode) nd;

        switch (pt.getToken().getType()) {

            case HiveParser.TOK_CREATETABLE:
                OutputTableList.add(BaseSemanticAnalyzer.getUnescapedName((ASTNode)pt.getChild(0)));
                break;
            case HiveParser.TOK_TAB:
                OutputTableList.add(BaseSemanticAnalyzer.getUnescapedName((ASTNode)pt.getChild(0)));
                break;
            case HiveParser.TOK_TABREF:
                ASTNode tabTree = (ASTNode) pt.getChild(0);
                String table_name = (tabTree.getChildCount() == 1) ?
                        BaseSemanticAnalyzer.getUnescapedName((ASTNode)tabTree.getChild(0)) :
                        BaseSemanticAnalyzer.getUnescapedName((ASTNode)tabTree.getChild(0)) + "." + tabTree.getChild(1);
                inputTableList.add(table_name);
                break;
        }
        return null;
    }

    /**
     * parses given query and gets the lineage info.
     *
     * @param query
     * @throws ParseException
     */
    public void getLineageInfo(String query) throws ParseException, SemanticException {

        /*
         * Get the AST tree
         */
        ParseDriver pd = new ParseDriver();
        ASTNode tree = pd.parse(query);

        while ((tree.getToken() == null) && (tree.getChildCount() > 0)) {
            tree = (ASTNode) tree.getChild(0);
        }

        /*
         * initialize Event Processor and dispatcher.
         */
        inputTableList.clear();
        OutputTableList.clear();

        // create a walker which walks the tree in a DFS manner while maintaining
        // the operator stack. The dispatcher
        // generates the plan from the operator tree
        Map<Rule, NodeProcessor> rules = new LinkedHashMap<Rule,NodeProcessor>();

        // The dispatcher fires the processor corresponding to the closest matching
        // rule and passes the context along
        Dispatcher disp = new DefaultRuleDispatcher(this, rules, null);
        GraphWalker ogw = new DefaultGraphWalker(disp);

        // Create a list of topop nodes
        ArrayList topNodes = new ArrayList();
        topNodes.add(tree);
        ogw.startWalking(topNodes, null);
    }

    public void clearResult(){
        inputTableList.clear();
        OutputTableList.clear();
    }


    public static void main(String[] args) throws IOException, ParseException,SemanticException {
//        String query = "create table bi_report.temp_t_day_asset_apply_merchant_002_ as SELECT concat(c.cert_number,c.dim_apply_date_id ) as id,c.cert_number,c.contract_number,c.dim_apply_date_id as apply_date   FROM (select   cert_number,max(apply_time) as apply_time FROM   bi_dm.fact_risk_credit_apply where  ts='111' and dim_apply_date_id>='' and audit_status_name   ='拒绝' GROUP BY cert_number ) t  INNER JOIN  (select cert_number ,contract_number,dim_apply_date_id,apply_time from bi_dm.fact_risk_credit_apply where ts='123') c ON c.cert_number =t.cert_number AND t.apply_time=c.apply_time";
        String query;//"insert overwrite table bi_report.t_day_asset_apply_merchant partition (ts='ts') select md5(concat_ws('_',risk.report_date,risk.sub_merchant_number))  as id,risk.report_date  as report_date,risk.sub_merchant_name      as    merchant_name_sub,risk.sub_merchant_number\tas    merchant_code_sub\t,risk.merchant_name as merchant_name,risk.merchant_code as merchant_code,risk.risk_apply_amount as risk_apply_amount,risk.risk_approve_amount    as    risk_approve_amount,risk.risk_refuse_amount     as    risk_refuse_amount,risk.risk_auditing_amount   as    risk_auditing_amount,risk.risk_apply_quantity\tas\t  risk_apply_quantity,risk.risk_approve_quantity\tas    risk_approve_quantity,risk.risk_refuse_quantity\tas    risk_refuse_quantity,risk.risk_auditing_quantity\tas    risk_auditing_quantity,nvl(asset.asset_apply_amount,0)\tas    asset_apply_amount,nvl(asset.asset_approve_amount,0)\tas    asset_approve_amount,nvl(asset.asset_refuse_amount,0)\tas    asset_refuse_amount,nvl(asset.asset_auditing_amount,0) as    asset_auditing_amount,nvl(asset.asset_apply_quantity,0)\tas    asset_apply_quantity,  nvl(asset.asset_approve_quantity,0) as   asset_approve_quantity,nvl(asset.asset_refuse_quantity,0)\t as   asset_refuse_quantity,nvl(asset.asset_auditing_quantity,0) as  asset_auditing_quantity,nvl(asset.traded_quantity,0) as   traded_quantity,nvl(asset.traded_amount,0)\tas   traded_amount,nvl(asset.create_time,current_timestamp())  as create_time,nvl(asset.update_time,current_timestamp())  as update_time, nvl(asset.creator_name,'LIKE')          as creator_name,nvl(asset.updater_name,'LIKE')          as updater_name from bi_report.temp_t_day_asset_apply_merchant_004_   risk left join bi_report.temp_t_day_asset_apply_merchant_005_ asset  on    risk.report_date=asset.report_date   and risk.sub_merchant_number=asset.sub_merchant_number";


        String sql = "/Users/xiang/Desktop/test.sql";
        try(BufferedReader reader = new BufferedReader(new FileReader(sql))){
            while(null != (query = reader.readLine())){
                parser.getLineageInfo(query);
                System.out.println("Input tables = " + parser.getInputTableList());
                System.out.println("Output tables = " + parser.getOutputTableList());
                parser.clearResult();

                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Map<String, Set<String>> parse(File hql){



        return null;
    }

}