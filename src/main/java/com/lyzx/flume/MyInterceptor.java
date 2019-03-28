//package com.lyzx.flume;
//
//import org.apache.flume.Context;
//import org.apache.flume.Event;
//import org.apache.flume.interceptor.Interceptor;
//import java.util.List;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//
//public class MyInterceptor implements Interceptor {
//    private static final Logger logger = LogManager.getLogger("com.jufan.worker.EventProcessor");
//    private static String param = "";
//
//    public MyInterceptor(){
//        logger.info("============");
//    }
//
//
//    @Override
//    public void initialize() {
//        logger.info("............MyInterceptor init......");
//    }
//
//    @Override
//    public Event intercept(Event event){
//        logger.info("intercept(event)..."+event);
//        return event;
//    }
//
//    @Override
//    public List<Event> intercept(List<Event> list) {
//        logger.info("intercept(List<Event> list)....");
//        for(Event e : list){
//           intercept(e);
//        }
//        return list;
//    }
//
//    @Override
//    public void close(){
//        logger.info("close....");
//    }
//
//    public static class Builder implements Interceptor.Builder {
//
//        /**
//         * 该方法主要用来返回创建的自定义类拦截器对象
//         *
//         * @return
//         */
//        @Override
//        public Interceptor build() {
//            logger.info("----------build方法执行");
//            return new MyInterceptor();
//        }
//
//        /**
//         * 用来接收flume配置自定义拦截器参数
//         *
//         * @param context 通过该对象可以获取flume配置自定义拦截器的参数
//         */
//        @Override
//        public void configure(Context context){
//            logger.info("----------configure方法执行-----"+context);
//            param = context.getString("param");
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.printf("===========");
//    }
//}
