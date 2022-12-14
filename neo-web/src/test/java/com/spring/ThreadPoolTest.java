package com.spring;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolTest
 *
 * @author home
 */

public class ThreadPoolTest {

    static class TomcatBlockingQueue<E> extends ArrayBlockingQueue<E> {

        private MyThreadExecutor parent;

        public void setParent(MyThreadExecutor parent) {
            this.parent = parent;
        }

        public TomcatBlockingQueue(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(E o) {
            ThreadPoolExecutor executor = parent.getExecutor();
            if (executor == null) {
                return super.offer(o);
            }
            //we are maxed out on threads, simply queue the object
            if (executor.getPoolSize() == executor.getMaximumPoolSize()) {
                return super.offer(o);
            }
            //we have idle threads, just add it to the queue
            if (parent.getTaskSize().get() <= executor.getPoolSize()) {
                return super.offer(o);
            }
            //if we have less threads than maximum force creation of a new thread
            if (executor.getPoolSize() < executor.getMaximumPoolSize()) {
                return false;
            }
            //if we reached here, we need to add it to the queue
            return super.offer(o);
        }

        public boolean force(E e) {
            return super.offer(e);
        }
    }


    static class MyThreadExecutor {

        protected ThreadPoolExecutor executor;

        private AtomicInteger taskSize = new AtomicInteger(0);

        public MyThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                TomcatBlockingQueue<Runnable> workQueue) {
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            workQueue.setParent(this);
        }

        public void execute(Runnable runnable) {
            try {
                taskSize.getAndIncrement();
                getExecutor().execute(runnable);
            } catch (RejectedExecutionException rejectedExecution) {
                if (!((TomcatBlockingQueue) executor.getQueue()).force(runnable)) {
                    throw new RejectedExecutionException("standardThreadExecutor.queueFull");
                }
            } finally {
                taskSize.decrementAndGet();
            }
        }

        public AtomicInteger getTaskSize() {
            return taskSize;
        }

        public void setTaskSize(AtomicInteger taskSize) {
            this.taskSize = taskSize;
        }

        public ThreadPoolExecutor getExecutor() {
            return executor;
        }

        public void setExecutor(ThreadPoolExecutor executor) {
            this.executor = executor;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThreadExecutor myThreadExecutor = new MyThreadExecutor(1, 2, 1, TimeUnit.HOURS, new TomcatBlockingQueue<>(2));
        myThreadExecutor.execute(() -> System.out.println("args = "));
    }

    static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
