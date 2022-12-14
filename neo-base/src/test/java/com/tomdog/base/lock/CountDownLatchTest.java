package com.tomdog.base.lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatchDemo
 * <p>
 * await() 线程阻塞，直到计数为0的时候唤醒；可以响应线程中断退出阻塞
 * <p>
 * await(long timeout, TimeUnit unit) 线程阻塞一段时间，如果计数依然不是0，则返回false；否则返回true
 * <p>
 * countDown() 每调用一次减一
 */
public class CountDownLatchTest {

    private CountDownLatch countDownLatch;

    private int start = 10;
    private int mid = 100;
    private int end = 200;

    private volatile int tmpRes1, tmpRes2;

    private int add(int start, int end) {
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }

    private int sum(int a, int b) {
        return a + b;
    }

    @Test
    public void calculate() {

        countDownLatch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {

                // 确保线程3先与1，2执行，由于countDownLatch计数不为0而阻塞
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " : 开始执行");
                tmpRes1 = add(start, mid);
                System.out.println(Thread.currentThread().getName() + " : calculate ans: " + tmpRes1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }, "线程1");

        Thread thread2 = new Thread(() -> {
            try {
                // 确保线程3先与1，2执行，由于countDownLatch计数不为0而阻塞
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " : 开始执行");
                tmpRes2 = add(mid + 1, end);
                System.out.println(Thread.currentThread().getName() + " : calculate ans: " + tmpRes2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }, "线程2");


        Thread thread3 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " : 开始执行");
                countDownLatch.await();
                int ans = sum(tmpRes1, tmpRes2);
                System.out.println(Thread.currentThread().getName() + " : calculate ans: " + ans);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程3");

        thread3.start();
        thread1.start();
        thread2.start();
    }

    private static CountDownLatch startSignal = new CountDownLatch(1);

    //用来表示裁判员需要维护的是6个运动员
    private static CountDownLatch endSignal = new CountDownLatch(6);

    @Test
    public void sport() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(6);

        for (int i = 0; i < 6; i++) {

            executorService.execute(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + " 运动员等待裁判员响哨！！！");

                    startSignal.await();

                    System.out.println(Thread.currentThread().getName() + "正在全力冲刺");

                    // 优先保证 endSignal.await() 先执行
                    Thread.sleep(100);
                    System.out.println(Thread.currentThread().getName() + "  到达终点");
                    endSignal.countDown();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

        Thread.sleep(10);
        System.out.println("裁判员发号施令啦！！！");
        startSignal.countDown();
        endSignal.await();
        System.out.println("所有运动员到达终点，比赛结束！");
        executorService.shutdown();

    }

    private static final long COUNT = 10000000L;

    // -XX:-RestrictContended
//    @sun.misc.Contended
    private static class T {
        public volatile long x = 0L;
    }

    public static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    private static class T1 {
        // 缓存行64byte,long类型8byte，所以前后再加7个long类型数据，缓存行就不会生效
        public volatile long p1, p2, p3, p4, p5, p6, p7;
        public volatile long x = 0L;
        public volatile long p8, p9, p10, p11, p12, p13, p14;
    }

    public static T1[] arr1 = new T1[2];

    static {
        arr1[0] = new T1();
        arr1[1] = new T1();
    }

    @Test
    public void testCacheLinePadding() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr[0].x = i;
            }
            latch.countDown();
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr[1].x = i;
            }
            latch.countDown();
        });

        long startTime = System.nanoTime();
        t1.start();
        t2.start();
        latch.await();
        System.out.println((System.nanoTime() - startTime) / 100000);
    }

    /**
     * 不走缓存行的数据在高并发的情况下反而速度更快
     */
    @Test
    public void testCacheLinePadding1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr1[0].x = i;
            }
            latch.countDown();
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < COUNT; i++) {
                arr1[1].x = i;
            }
            latch.countDown();
        });

        long startTime = System.nanoTime();
        t1.start();
        t2.start();
        latch.await();
        System.out.println((System.nanoTime() - startTime) / 100000);
    }
}

