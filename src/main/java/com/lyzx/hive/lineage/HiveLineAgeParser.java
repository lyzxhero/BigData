package com.lyzx.hive.lineage;

import org.apache.hadoop.hive.ql.lib.*;
import org.apache.hadoop.hive.ql.parse.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hero
 * desc
 *   解析hive hql的查询语句以获得表级别的依赖关系
 */
public class HiveLineAgeParser implements NodeProcessor {
    private static final HiveLineAgeParser parser = new HiveLineAgeParser();
    private HiveLineAgeParser(){}

    // Stores input tables in sql.
    TreeSet inputTableList = new TreeSet();

    //Stores output tables in sql.
    TreeSet OutputTableList = new TreeSet();

    public TreeSet getInputTableList() {
        return inputTableList;
    }

    public TreeSet getOutputTableList() {
        return OutputTableList;
    }

     // Implements the process method for the NodeProcessor interface.
    public Object process(Node nd, Stack stack, NodeProcessorCtx procCtx, Object... nodeOutputs) {
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
    public void getLineageInfo(String query) throws ParseException,SemanticException {
        //Get the AST tree
        ParseDriver pd = new ParseDriver();
        ASTNode tree = pd.parse(query);

        while ((tree.getToken() == null) && (tree.getChildCount() > 0)) {
            tree = (ASTNode) tree.getChild(0);
        }

        //  initialize Event Processor and dispatcher.
        inputTableList.clear();
        OutputTableList.clear();

        // create a walker which walks the tree in a DFS manner while maintaining
        // the operator stack. The dispatcher
        // generates the plan from the operator tree
        Map<Rule,NodeProcessor> rules = new LinkedHashMap<>();

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

    /**
     *
     * @param hql
     * @return
     */
    public static Map<String, Set<String>> parse(File hql){
        if(null == hql || !hql.exists() || !hql.isFile()){
            throw new RuntimeException("args is err:"+hql);
        }

        Set<String> inTableSet = new TreeSet<>();
        Set<String> outTableSet = new TreeSet<>();

        List<String> sqlBlockList = HqlReader.readHql(hql);
        for(int i=0;i<sqlBlockList.size();i++){
            try {
                String sqlBlock =  sqlBlockList.get(i);
                parser.getLineageInfo(sqlBlock);
                TreeSet inTables = parser.getInputTableList();
                TreeSet outTables = parser.getOutputTableList();

                inTableSet.addAll(inTables);
                if(i != sqlBlockList.size()-1){
                    inTableSet.addAll(outTables);
                }else{
                    outTableSet.addAll(outTables);
                }

                parser.clearResult();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SemanticException e) {
                e.printStackTrace();
            }
        }


        inTableSet = inTableSet.stream()
                    .filter(item -> !item.contains(".temp_"))
                    .collect(Collectors.toSet());

        Map<String,Set<String>> result = new HashMap<>();
        result.put("in",inTableSet);
        result.put("out",outTableSet);
        return result;
    }
}
