package bitbucket.neo.thread.lock;

import bitbucket.neo.thread.pool.ThreadPoolUtil;

/**
 * 哈哈，一个死锁
 *
 * @author liujunjie
 */
public class DeadLockDemo {

    private final static String RESOURCE_A = "A";
    private final static String RESOURCE_B = "B";

    public static void main(String[] args) {
        deadLock();
    }

    public static void deadLock() {

        ThreadPoolUtil.getInstance().execute(() -> {
            synchronized (RESOURCE_A) {
                System.out.println("get resource a1");
                try {
                    Thread.sleep(3000);
                    synchronized (RESOURCE_B) {
                        System.out.println("get resource b1");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ThreadPoolUtil.getInstance().execute(() -> {
            synchronized (RESOURCE_B) {
                System.out.println("get resource b2");
                synchronized (RESOURCE_A) {
                    System.out.println("get resource a2");
                }
            }
        });

    }
}
