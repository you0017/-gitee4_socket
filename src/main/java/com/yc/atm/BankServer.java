package com.yc.atm;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BankServer {
    private static Logger log = Logger.getLogger(BankServer.class);
    public static void main(String[] args) throws IOException {
        //cpu核数
        int processors = Runtime.getRuntime().availableProcessors();
        //核心线程池的大小
        int corePoolSize = processors;
        //核心线程池的最大线程数
        int maximumPoolSize = processors * 2;
        //线程池的存活时间
        long keepAliveTime = 10;
        //时间单位
        TimeUnit unit = TimeUnit.SECONDS;    //enum枚举
        //阻塞队列，容量为2
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(200);
        //线程工厂
        ThreadFactory threadFactory = new NameThreadFactory();
        //线程池的拒绝策略
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        //线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory, handler);
        //预启动所有核心线程
        executor.prestartAllCoreThreads();



        //创建银行
        Bank b = new Bank();
        ServerSocket ss = new ServerSocket(12000);
        log.info("银行服务器启动，监听"+ss.getLocalPort()+"端口");
        boolean flag = true;
        while (flag){
            Socket s = ss.accept();
            System.out.println("ATM客户端："+s.getInetAddress().getHostAddress()+"登录了服务器");
            //TODO: 商业项目必须要用线程池
            BankTask task = new BankTask(s,b);
            //提交任务到线程池
            executor.submit(task);

        }

        executor.shutdown();
    }


    /**
     * 线程工厂
     */
    static class NameThreadFactory implements ThreadFactory{
        private AtomicInteger threadId = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,"线程-"+threadId.getAndIncrement());
            return t;
        }
    }

    /**
     * 线程池拒绝策略
     */
    public static class MyIgnorePolicy implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            doLog(r,executor);
        }

        private void doLog(Runnable r,ThreadPoolExecutor executor) {
            log.info("线程池已满，任务被拒绝执行");
            log.info("线程池的当前线程数：" + executor.getPoolSize());
            log.info("线程池的当前队列大小：" + executor.getQueue().size());
            log.info("线程池的当前活动线程数：" + executor.getActiveCount());
        }
    }
}
