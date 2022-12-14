package com.tomdog.base;

import org.junit.Test;

import java.io.IOException;
import java.lang.ref.*;
import java.util.LinkedList;
import java.util.List;

public class ReferenceTest {

    public class Reference {
        @Override
        protected void finalize() {
            System.out.println("gc finalize");
        }
    }

    // 强引用
    @Test
    public void NormalReference() throws IOException {
        Reference o = new Reference();
        o = null;
        System.gc();
        System.in.read();
    }

    // 软引用，缓存应用
    @Test
    public void SoftReference() throws IOException, InterruptedException {
        SoftReference<byte[]> m = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(m.get());
        System.gc();
        Thread.sleep(500);
        System.out.println(m.get());
        // -Xmx20M 内存不足
        byte[] b = new byte[1024 * 1024 * 15];
        System.out.println(m.get());
    }

    // 弱引用,只要发生gd就会回收
    @Test
    public void WeakReference() {
        WeakReference<Reference> m = new WeakReference<>(new Reference());
        System.out.println(m.get());
        System.gc();
        System.out.println(m.get());

        ThreadLocal<Reference> t1 = new ThreadLocal<>();
        t1.set(new Reference());
        t1.remove();
    }


    private static final List<Object> LIST = new LinkedList<>();
    private static final ReferenceQueue<Reference> QUEUE = new ReferenceQueue<>();

    // 虚引用,jvm特殊的gc线程管理堆外内存，管理操作系统内存，DirectByteBuffer
    @Test
    public void PhantomReference() throws InterruptedException {

        PhantomReference<Reference> phantomReference = new PhantomReference<>(new Reference(), QUEUE);
        Thread thread1 = new Thread(() -> {
            while (true) {
                LIST.add(new byte[1024 * 1024]);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                System.out.println(phantomReference.get());
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                java.lang.ref.Reference<? extends Reference> poll = QUEUE.poll();
                if (poll != null) {
                    System.out.println("gc happen" + poll);
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
