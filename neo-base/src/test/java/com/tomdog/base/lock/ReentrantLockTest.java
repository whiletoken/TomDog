package com.tomdog.base.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    condition.await();
                } catch (Exception e) {
                    lock.unlock();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    System.out.println("thread2");
                }catch (Exception e){

                }finally {
                    lock.unlock();
                    System.out.println("unlock");
                }
            }
        });

        thread1.start();
        thread2.start();

        System.out.println("args = 123");
    }

}
