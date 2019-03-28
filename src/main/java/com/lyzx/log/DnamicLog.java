package com.lyzx.log;
//
//import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.Layout;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.RollingFileAppender;
//import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
//import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.action.Action;
//import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
//import org.apache.logging.log4j.core.appender.rolling.action.Duration;
//import org.apache.logging.log4j.core.appender.rolling.action.IfLastModified;
//import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
//import org.apache.logging.log4j.core.config.AppenderRef;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.apache.logging.log4j.core.layout.PatternLayout;
//import java.io.File;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//import java.util.Set;
//
public class DnamicLog {
//    /**日志打印的目录*/
//    private static final String datalogDir = "/Users/xiang/Downloads/log";
//    private static final String logName = "front-event-data";
//    private static final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//    private static final Configuration config = ctx.getConfiguration();
//
//    private DnamicLog(){}
//
//    /**启动一个动态的logger*/
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    private static void start(String platform){
//
//        //创建一个展示的样式：PatternLayout，   还有其他的日志打印样式。
//        Layout layout = PatternLayout
//                            .newBuilder()
//                            .withConfiguration(config)
//                            .build();
//
//        //单个日志文件大小
//        TimeBasedTriggeringPolicy tbtp = TimeBasedTriggeringPolicy.createPolicy("1", "true");
//        CompositeTriggeringPolicy policyComposite = CompositeTriggeringPolicy.createPolicy(tbtp);
//
//        //删除日志的条件  默认保持60天的日志消息
//        IfLastModified ifLastModified = IfLastModified.createAgeCondition(Duration.parse("60d"));
//        DeleteAction deleteAction = DeleteAction.createDeleteAction(
//                datalogDir, false, 2, false, null,
//                new PathCondition[]{ifLastModified}, null, config);
//
//        Action[] actions = new Action[]{deleteAction};
//        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(
//                "100", "60", null, null,actions,false,config);
//
//        String loggerPathPrefix = datalogDir+File.separator+platform+File.separator+ logName;
//        RollingFileAppender appender =
//                RollingFileAppender
//                        .newBuilder()
//                        .withFileName(loggerPathPrefix+".log")
//                        .withFilePattern(loggerPathPrefix + ".%d{yyyy-MM-dd}.log")
//                        .withAppend(true)
//                        .withStrategy(strategy)
//                        .withName(platform)
//                        .withPolicy(policyComposite)
//                        .withLayout(layout)
//                        .withConfiguration(config)
//                        .build();
//        appender.start();
//
//        config.addAppender(appender);
//
//        AppenderRef ref = AppenderRef.createAppenderRef(platform, null, null);
//        AppenderRef[] refs = new AppenderRef[]{ref};
//        LoggerConfig loggerConfig = LoggerConfig.createLogger(false,Level.ALL,platform,"true", refs, null, config, null);
//        loggerConfig.addAppender(appender,null,null);
//        config.addLogger(platform, loggerConfig);
//        ctx.updateLoggers();
//    }
//
//    /**使用完之后记得调用此方法关闭动态创建的logger，避免内存不够用或者文件打开太多*/
//    public static void stop(){
//        synchronized(config){
//            Set<Map.Entry<String, Appender>> entrySet = config.getAppenders().entrySet();
//            for(Map.Entry<String, Appender> item : entrySet){
//                String key = item.getKey();
//                item.getValue().stop();
//                config.getLoggerConfig(key).removeAppender(key);
//                config.removeLogger(key);
//            }
//            ctx.updateLoggers();
//        }
//    }
//
//    /**获取Logger*/
//    public static Logger getLogger(String platform) {
//        if(!config.getLoggers().containsKey(platform)){
//            synchronized(config) {
//                if(!config.getLoggers().containsKey(platform)){
//                    start(platform);
//                }
//            }
//        }
//        return LogManager.getLogger(platform);
//    }
//
//    public static void main(String[] args){
//        for(int i = 0; i < 100; i++){
//            try{Thread.sleep(10);}catch(InterruptedException e){e.printStackTrace();}
//            String name = "s" + String.valueOf(i%2);
//            Logger logger = getLogger(name);
//            logger.info("==>"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        }
//        stop();
//    }
}