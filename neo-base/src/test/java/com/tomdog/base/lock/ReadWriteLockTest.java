package com.tomdog.base.lock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ReadWriteLockTest {

    @Test
    public void testLock() {

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        Lock readLock = readWriteLock.readLock();

        Lock writeLock = readWriteLock.writeLock();

        readLock.lock();

        try {

            // 读写锁不支持锁升级，所以这个地方并发会导致死锁
            writeLock.lock();
        } catch (Exception e) {
        } finally {
            readLock.unlock();
        }

    }

}
