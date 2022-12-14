package com.tomdog.gateway.util.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 压榨CPU性能的线程阻塞队列
 * 一般用于需要能够快速处理的任务，例如网络请求，尽最大努力处理，减少响应时间
 *
 * @param <E>
 */
public class TomcatBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private ThreadPriorityPoolExecutor parent;

    public void setParent(ThreadPriorityPoolExecutor parent) {
        this.parent = parent;
    }

    public TomcatBlockingQueue(int capacity) {
        super(capacity);
    }

    /**
     * 实现核心
     *
     * @param o the element to add
     * @return boolean
     */
    @Override
    public boolean offer(E o) {
        ThreadPoolExecutor executor = parent.getExecutor();
        if (executor == null) {
            return super.offer(o);
        }
        // 达到线程数上限，任务压进队列等待处理
        if (executor.getPoolSize() == executor.getMaximumPoolSize()) {
            return super.offer(o);
        }
        // 待完成任务数小于当前线程数，说明存在空闲线程，任务压进队列
        if (parent.getTaskSize().get() <= executor.getPoolSize()) {
            return super.offer(o);
        }
        // 未达到最大线程数，返回false,new thread 处理任务，压榨CPU
        if (executor.getPoolSize() < executor.getMaximumPoolSize()) {
            return false;
        }
        // 条件判断走到这里，说明存在其他并行任务，任务继续
        return super.offer(o);
    }

    public boolean force(E e) {
        if (parent.getExecutor() == null || parent.getExecutor().isShutdown()) {
            throw new RejectedExecutionException("taskQueue.notRunning");
        }
        return super.offer(e);
    }

}
