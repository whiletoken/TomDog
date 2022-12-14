package bitbucket.neo.thread.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPool
 * <p>
 * 1、初始化，核心线程采取懒加载方式来加载
 * <p>
 * ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。
 * LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。
 * PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
 * DelayQueue：一个使用优先级队列实现的无界阻塞队列。
 * SynchronousQueue：一个不存储元素的阻塞队列。
 * LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
 * LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。
 *
 * @author willian
 **/

public final class ThreadPoolUtil {

    static volatile ThreadPoolExecutor threadPoolExecutor = null;

    public static ThreadPoolExecutor getInstance() {

        if (threadPoolExecutor == null) {

            synchronized (ThreadPoolUtil.class) {

                if (threadPoolExecutor == null) {

                    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool" + "-%d").setDaemon(true).build();

                    ArrayBlockingQueue<Runnable> linkedBlockingQueue = new ArrayBlockingQueue<>(1024);
                    threadPoolExecutor = new ThreadPoolExecutorMdcWrapper(6, 16, 30L,
                            TimeUnit.SECONDS, linkedBlockingQueue, threadFactory, "pool-util");
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                    return threadPoolExecutor;
                }
            }
        }
        return threadPoolExecutor;
    }

}
