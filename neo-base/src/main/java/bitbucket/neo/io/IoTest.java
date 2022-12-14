package bitbucket.neo.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 装饰器模式
 */
public class IoTest {

    public static void main(String[] args) throws IOException {
        //读取文件(缓存字节流)
        BufferedInputStream in = new BufferedInputStream(Files.newInputStream(Paths.get("/home/lina/桌面/document.txt")));
        //写入相应的文件
        BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get("/home/lina/桌面/123.txt")));
        //读取数据
        //一次性取多少字节
        byte[] bytes = new byte[8];
        //接受读取的内容(n就代表的相关数据，只不过是数字的形式)
        int n = -1;
        //循环取出数据
        while ((n = in.read(bytes, 0, bytes.length)) != -1) {
            //转换成字符串
            String str = new String(bytes, 0, n, "GBK");
            System.out.println(str);
            //写入相关文件
            out.write(bytes, 0, n);
        }
        //清楚缓存
        out.flush();
        //关闭流
        in.close();
        out.close();
    }

}
