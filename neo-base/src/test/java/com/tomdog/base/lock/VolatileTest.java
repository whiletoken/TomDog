package com.tomdog.base.lock;

import org.junit.Test;

/**
 * cpu、内存，两者之间通过总线传输数据
 * <p>
 * 1、数据总线；2、控制总线；3、地址总线
 * <p>
 * 进程加载到内存，cpu读取执行main方法，cpu把数据写回内存
 * cpu执行速度大概是内存的一百倍，所以在两者之间增加三级缓存 L1、L2、L3 cache
 * 寄存器、L1 cache、L2 cache、L3 cache、内存 多级读取和写入
 * 多颗cpu和多核cpu，一颗cpu包含多核 L1、L2 cache是单核cpu独享，L3 cache是单颗cpu共享
 * 线程切换 程序计数器、寄存器、ALU
 * 4核8线程 一个cpu单元模拟出两组程序计数器和寄存器
 * <p>
 * 程序局部性原理：在读取内存中数据时，会同时读取该数据相邻位置的数据，为了增加效率
 * 缓存行：一行为64 byte 太大，读取慢；太小，空间效率低 缓存按照“行”的方式读取数据
 * MESI：因特尔cpu特有
 * <p>
 * 1、内存可见性；2、禁止指令重排序
 * <p>
 * jvm内存屏障，屏障两边的指令不可以重排！保障有序，LoadLoad、LoadStore、StoreStore、StoreLoadBarrier
 * ​happens-before
 * ​as-if-serial
 */
public class VolatileTest {

    /**
     * 1、new 开辟内存空间,成员变量赋默认值，半初始化对象
     * 2、invokespecial，调用构造方法，成员变量赋值
     * 3、astore 将堆内存地址和引用对象建立关联
     * <p>
     * 指令重排序可能会导致2、3的顺序发生乱序，这样单例就会拿到半初始化对象
     */
    @Test
    public void testObject() {
        Object object = new Object();
    }

}
