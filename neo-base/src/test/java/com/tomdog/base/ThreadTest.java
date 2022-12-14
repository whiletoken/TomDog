package com.tomdog.base;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1、NEW -> Ready -> Running -> Terminated
 * <p>
 * 2、Ready -> Waiting -> Running
 * o.wait()、t.join()、LockSupport.parkUtil()
 * o.notify()、o.notifyAll()、LockSupport.unpark()
 * <p>
 * 3、Ready —> TimedWaiting -> Running
 * Thread.sleep(time)、o.wait(time)、t.join(time)、LockSupport.parkNanos()、LockSupport.parkUtil()
 * 时间结束
 * <p>
 * 4、Ready —> Blocked -> Running
 * 等待进入同步代码块的锁
 * 获得锁
 */
public class ThreadTest {

    @Test
    public void testSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    // 从运行状态返回到就绪状态
    @Test
    public void testYield() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("B" + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        });

        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
    }

    @Test
    public void testJoin() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                // 等待thread先运行完
                thread.join();
                System.out.println("B is Over");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Runnable runnable = () -> {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("end time is " + (System.currentTimeMillis() - startTime) + " thread name is " + Thread.currentThread().getName());
            lock.unlock();
        };

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(runnable);
            thread.setName("thread-" + i);
            thread.start();
        }

        System.out.println("end1 = " + (System.currentTimeMillis() - startTime));

        Thread.sleep(1000);
        lock.lock();
        condition.signalAll();
        lock.unlock();

        System.out.println("end2 = " + (System.currentTimeMillis() - startTime));
    }

}
