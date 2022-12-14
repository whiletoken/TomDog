package com.tomdog.base.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {

    //指定必须有6个运动员到达才行
    private static CyclicBarrier barrier = new CyclicBarrier(6, () -> {

        System.out.println("所有运动员入场，裁判员一声令下！！！！！");

    });

    public static void main(String[] args) {

        System.out.println("运动员准备进场，全场欢呼............");

        ExecutorService service = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 12; i++) {

            service.execute(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + " 运动员，进场");

                    barrier.await();

                    System.out.println(Thread.currentThread().getName() + "  运动员出发");

                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

        }
    }

}
