package bitbucket.neo.collection.map;

import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * RoaringMap
 * <p>
 * RoaringBitMap使用的以下位图索引压缩算法：
 * 1.我们将 32-bit 的范围 ([0, n)) 划分为 2^16 个桶，每一个桶有一个 Container 来存放一个数值的低16位；
 * 2.在存储和查询数值的时候，我们将一个数值 k 划分为高 16 位(k % 2^16)和低 16 位(k mod 2^16)，取高 16 位找到对应的桶，然后在低 16 位存放在相应的 Container 中；
 * 3.容器的话， RBM 使用两种容器结构： Array Container 和 Bitmap Container。Array Container 存放稀疏的数据，Bitmap Container 存放稠密的数据。
 * 即，若一个 Container 里面的 Integer 数量小于 4096，就用 Short 类型的有序数组来存储值。若大于 4096，就用 Bitmap 来存储值。
 * <p>
 *
 * @author home
 */
public class RoaringMap {

    private static RoaringBitmap roaringBitmap = new RoaringBitmap();

    public static void main(String[] args) {

        Runtime rt = Runtime.getRuntime();
        System.out.println("当前JVM所占内存：" + (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024 + "M");
        insertFromTxt();
        System.out.println("当前JVM所占内存：" + (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024 + "M");
        System.out.println(roaringBitmap.contains(88888888));
        System.out.println(roaringBitmap.contains(99999999));
        System.out.println(roaringBitmap.contains(91725151));
    }

    @Test
    public void writeData() throws Exception {
        insertFromTxt();
        //序列化
        FileOutputStream fos = new FileOutputStream("/Users/home/workspace/myproject/neo-base/data.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        roaringBitmap.serialize(dos);
        //数据刷新到磁盘
        dos.flush();
        dos.close();
    }

    @Test
    public void read() throws Exception {

        //读取二进制流并包装成ByteBuf
        FileInputStream fi = new FileInputStream("/Users/home/workspace/myproject/neo-base/data.txt");
        DataInputStream di = new DataInputStream(fi);
        byte[] bytes = new byte[di.available()];
        di.read(bytes, 0, di.available());
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        //ByteBuf还原数据结构
        ImmutableRoaringBitmap immutableRoaringBitmap = new ImmutableRoaringBitmap(bb);
        roaringBitmap = new RoaringBitmap(immutableRoaringBitmap);

        System.out.println(roaringBitmap.contains(88888888));
        System.out.println(roaringBitmap.contains(99999999));
        System.out.println(roaringBitmap.contains(91725151));
    }

    public static void insertFromTxt() {
        try {
            File file = new File("/Users/home/workspace/myproject/neo-base/numbers.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                roaringBitmap.add(Integer.parseInt(str));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void makeNumbers() {
        Random random = new Random();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("numbers.txt"));
            for (int i = 0; i < 1000000000; i++) {
                bw.write(String.valueOf(Math.abs(random.nextInt(1000000000))));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
