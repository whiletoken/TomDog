package com.tomdog.base.lock;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * sync
 * <p>
 * 对象所有的信息都会在线程本地栈（Lock Record）中存储一份
 * <p>
 * 1、mark word 64位jvm占用8个字节  锁信息、hashcode、gc分代年龄（4个比特，最大15）
 * 2、成员变量
 * 3、对象补齐 被8整除
 * <p>
 * 用户态、内核态：自旋锁在用户态空间下工作，重量级锁在内核态空间下工作
 * 大多数情况下，不会发生资源竞争，所以产生了锁升级的策略
 * <p>
 * 无锁：001、偏向锁（匿名偏向、线程id偏向）：101、轻度锁：
 * 在明知道存在大量锁竞争的情况下，没必要启动偏向锁
 * <p>
 * 加锁、锁撤销
 * <p>
 * 锁升级过程：普通对象 -> 偏向锁（偏向第一个持有该锁的对象 线程id 是否持有锁 所级别） (轻度竞争升级)-> 轻量级锁（自旋锁）
 * (重度竞争升级 自旋超过10次 等待线程数超过cpu核数一半)-> 重量级锁（只有一个线程，其它线程阻塞）
 */
public class SynchronizedTest {

    private String sex;
    private String number;

    private static class Student {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testWait() throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread1 = new Thread(() -> {
                synchronized (SynchronizedTest.class) {
                    try {
                        SynchronizedTest.class.wait();
                        System.out.printf("thread%s finished%n", finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i] = thread1;
        }
        for (Thread thread : threads) {
            thread.start();
        }
//        SynchronizedTest.class.notify();
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        synchronized (SynchronizedTest.class) {
            SynchronizedTest.class.notify();
        }

//        Thread.sleep(1000);
    }

    @Test
    public void testSync() {

        SynchronizedTest object = new SynchronizedTest();

        Student student = new Student();

        System.out.println(ClassLayout.parseInstance(object).toPrintable());
        System.out.println(ClassLayout.parseInstance(student).toPrintable());

        /*mark word 发生改变*/
//        synchronized (object) {
//            System.out.println(ClassLayout.parseInstance(object).toPrintable());
//        }
//
//        System.out.println(ClassLayout.parseInstance(new String()).toPrintable());

    }

    private static final String lock = "lock";

    @Test
    public void testWait2() throws InterruptedException {
        synchronized (lock) {
            this.wait();
            System.out.println("123123123");
        }
        System.out.println("123");
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
