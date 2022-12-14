package bitbucket.neo.zk;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ZookClient {

    private final String connectString;
    private final String authorization;
    private volatile boolean upFlag = false;

    private final ReentrantLock lock = new ReentrantLock();

    private static CuratorFramework client = null;

    public ZookClient(String host, String port, String user, String password) {
        this.connectString = host + ":" + port;
        this.authorization = user + ":" + password;
    }

    public void init() {
        if (!upFlag) {
            try {
                lock.lock();
                if (!upFlag) {
                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    client = CuratorFrameworkFactory.builder()
                            .connectString(connectString)
                            .authorization("digest", authorization.getBytes())
                            .sessionTimeoutMs(3000)
                            .connectionTimeoutMs(5000)
                            .retryPolicy(retryPolicy)
                            .build();
                    client.start();
                    upFlag = true;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 同步创建节点
     * 注意：
     * 1.除非指明创建节点的类型,默认是持久节点
     * 2.ZooKeeper规定:所有非叶子节点都是持久节点,所以递归创建出来的节点,
     * 只有最后的数据节点才是指定类型的节点,其父节点是持久节点
     * org.apache.zookeeper.CreateMode
     */
    public void create(String path, String value, int createMode) throws Exception {
        if (client.checkExists().forPath(path) != null) {
            log.info("path is {}, node exists", path);
            return;
        }
        CreateBuilder createBuilder = client.create();
        create(createBuilder, path, value, createMode);
    }

    private void create(CreateBuilder createBuilder, String path, String value, int createMode) throws Exception {
        switch (createMode) {
            case 0 -> createBuilder.creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, value.getBytes());
            case 1 -> createBuilder.creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, value.getBytes());
            case 2 -> createBuilder.creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path, value.getBytes());
            case 3 -> createBuilder.creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path, value.getBytes());
            default -> {
                String errMsg = "Received an invalid flag value: " + createMode + " to convert to a CreateMode";
                throw new KeeperException.BadArgumentsException(errMsg);
            }
        }
    }

    /**
     * 异步创建节点
     * <p>
     * 注意:如果自己指定了线程池,那么相应的操作就会在线程池中执行,如果没有指定,
     * 那么就会使用Zookeeper的EventThread线程对事件进行串行处理
     */
    public void asyncCreate(String path, String value, int createMode) throws Exception {
        CreateBuilder createBuilder = client.create();
        createBuilder.inBackground((client, event) -> System.out.println("当前线程：" + Thread.currentThread().getName() + ",code:"
                + event.getResultCode() + ",type:" + event.getType()), Executors.newCachedThreadPool(ThreadFactoryBuilder.create().setNamePrefix("zk-").build()));
        create(createBuilder, path, value, createMode);
    }

    public byte[] getData(String path) throws Exception {
        Stat stat = new Stat();
        byte[] result = client.getData().storingStatIn(stat).forPath(path);
        log.info("stat is {}", stat);
        return result;
    }

    public void setData(String path, String value) throws Exception {
        Stat stat = client.setData().forPath(path, value.getBytes());
        log.info("stat is {}", stat);
    }

    /**
     * 只能删除叶子节点
     */
    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    public void deletingChildren(String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    public static void main(String[] args) throws Exception {

        ZookClient zookClient = new ZookClient("81.68.177.159", "2181", "lina", "zk123$%^");
        zookClient.init();
        String path = "/auth/China1";
        System.out.println("-----------------------------------");
        zookClient.create("/auth/China1", "No1", 0);
//        byte[] getData = zookClient.getData("/auth/China");
//        System.out.println("args = " + new String(getData));
        client.getData().usingWatcher((Watcher) watchedEvent -> System.out.println("监听到节点事件:" + JSONUtil.toJsonStr(watchedEvent))).forPath(path);
        //第一次更新
        client.setData().forPath(path, "1".getBytes());
        //第二次更新
        zookClient.getData(path);

        Thread.sleep(1000 * 500);
    }

}