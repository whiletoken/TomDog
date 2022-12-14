package com.tomdog.base.pool;

import bitbucket.neo.thread.pool.ThreadPoolParam;
import bitbucket.neo.thread.pool.ThreadPoolExecutorMdcWrapper;
import bitbucket.neo.util.MBeanServerUtil;
import org.junit.Test;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorsDemo
 *
 * @author arthas
 */
public class ExecutorsDemo {

    @Test
    public void newFixedThreadPool() {

        // Executors 线程静态工厂、LinkedBlockingQueue
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorService.execute(new Thread());
    }

    @Test
    public void newSingleThreadExecutor() {

        // Executors 线程静态工厂、LinkedBlockingQueue
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Thread());
    }

    @Test
    public void newSingleThreadScheduledExecutor() {

        // Executors 线程静态工厂、LinkedBlockingQueue
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.execute(new Thread());
    }

    @Test
    public void newCachedThreadPool() {

        // Executors 线程静态工厂、LinkedBlockingQueue
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Thread());
    }

    @Test
    public void AcquireResultTest() throws ExecutionException, InterruptedException {

        //getNow方法测试
        CompletableFuture<String> cp1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(60 * 1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "hello world";
        });

        System.out.println(cp1.getNow("hello h2t"));

        //join方法测试
        CompletableFuture<Integer> cp2 = CompletableFuture.supplyAsync((() -> 1 / 0));
        System.out.println(cp2.join());

        //get方法测试
        CompletableFuture<Integer> cp3 = CompletableFuture.supplyAsync((() -> 1 / 0));
        System.out.println(cp3.get());
    }


    @Test
    public void test() throws InterruptedException, MalformedObjectNameException {


        ExecutorService es1 = ThreadPoolExecutorMdcWrapper.newCachedThreadPool("test-pool-1");
        ThreadPoolParam threadPoolParam1 = new ThreadPoolParam(es1);

        ExecutorService es2 = ThreadPoolExecutorMdcWrapper.newCachedThreadPool("test-pool-2");
        ThreadPoolParam threadPoolParam2 = new ThreadPoolParam(es2);

        MBeanServerUtil.registerMBean(threadPoolParam1, new ObjectName("test-pool-1:type=threadPoolParam"));
        MBeanServerUtil.registerMBean(threadPoolParam2, new ObjectName("test-pool-2:type=threadPoolParam"));

        bitbucket.neo.HtmlAdaptor.start();

        executeTask(es1);
        executeTask(es2);
        Thread.sleep(1000 * 60 * 60);

    }

    private static Random random = new Random();

    private static void executeTask(ExecutorService es) {
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                int temp = i;
                es.submit(() -> {
                    //随机睡眠时间
                    try {
                        Thread.sleep(random.nextInt(60) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(temp);
                });
            }
        }).start();
    }

    @Test
    public void testCallerRunsPolicy() {
        ThreadPoolExecutorMdcWrapper threadPoolExecutorMdcWrapper =
                new ThreadPoolExecutorMdcWrapper(10, 10, 10, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(10), new ThreadPoolExecutor.CallerRunsPolicy(), "pool-1");

        for (int i = 0; i < 100; i++) {
            System.out.println("i is " + i);
            threadPoolExecutorMdcWrapper.execute(() -> {
                try {
                    System.out.println(java.lang.Thread.activeCount());
                    Thread.sleep(600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
