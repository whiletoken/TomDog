package com.tomdog.gateway.util.thread;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPriorityPoolExecutor
 * 线程优先级executor
 *
 * @author lina
 */

public class ThreadPriorityPoolExecutor {

    private final ThreadPoolExecutor executor;

    // 当前未完成的任务数量
    private final AtomicInteger taskSize = new AtomicInteger(0);

    public ThreadPriorityPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      TomcatBlockingQueue<Runnable> workQueue) {
        this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        workQueue.setParent(this);
    }

    public void execute(Runnable runnable) {
        try {
            this.taskSize.getAndIncrement();
            this.getExecutor().execute(runnable);
        } catch (RejectedExecutionException rejectedExecution) {
            // 线程池已满，尝试将任务压进队列
            if (!((TomcatBlockingQueue) this.executor.getQueue()).force(runnable)) {
                // 队列已满，任务被抛弃，这里也可以自定义处理拒绝策略
                throw new RejectedExecutionException("standardThreadExecutor.queueFull");
            }
        } finally {
            this.taskSize.decrementAndGet();
        }
    }

    public List<Runnable> shutdownNow() {
        return this.executor.shutdownNow();
    }

    public AtomicInteger getTaskSize() {
        return this.taskSize;
    }

    public ThreadPoolExecutor getExecutor() {
        return this.executor;
    }


}
