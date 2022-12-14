package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * Create Time: 2020/11/28
 *
 * @author <a href="mailto:liujj@shinemo.com">liujunjie</a>
 */
public class NIOServer {


    /**
     * serverSelector负责轮询是否有新的连接,clientSelector负责轮询连接是否有数据可读.
     * 服务端监测到新的连接不再创建一个新的线程,而是直接将新连接绑定到clientSelector上,这样不用IO模型中1w个while循环在死等
     * clientSelector被一个while死循环包裹,如果在某一时刻有多条连接有数据可读通过 clientSelector.select(1)方法轮询出来进而批量处理
     * 数据的读写以内存块为单位
     */
    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {

                // 监听客户端连接，是所有客户端连接的父通道
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                // 端口绑定，设置连接为非阻塞模式
                serverSocketChannel.socket().bind(new InetSocketAddress(8000));
                serverSocketChannel.configureBlocking(false);

                // 将serverSocketChannel注册到serverSelector，监听ACCEPT事件
                serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {

                    // 轮询监测是否有新的连接
                    if (serverSelector.select(1) <= 0) {
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (!selectionKey.isAcceptable()) {
                            continue;
                        }
                        try {
                            //(1)每来一个新连接不需要创建一个线程而是直接注册到clientSelector
                            // 多路复用监听到新客户端接入，处理新的接入请求，完成TCP三次握手，建立物理连接
                            SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(clientSelector, SelectionKey.OP_READ);
                        } finally {
                            keyIterator.remove();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {

                    // (2)批量轮询是否有哪些连接有数据可读
                    if (clientSelector.select(1) <= 0) {
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (!selectionKey.isReadable()) {
                            continue;
                        }
                        try {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                            //(3)读取数据以块为单位批量读取
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer));
                        } finally {
                            keyIterator.remove();
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void SimpleDemo() throws IOException {
        Selector selector = Selector.open();//打开一个Selector
        ServerSocketChannel ssc = ServerSocketChannel.open();//打开一个Channel
        ssc.configureBlocking(false);//非阻塞
        ssc.register(selector, SelectionKey.OP_ACCEPT);//注册到Selector并监听ACCEPT事件
        ssc.bind(new InetSocketAddress(8080));//绑定端口
        System.out.println("服务器启动成功！");
        while (true) {
            selector.select();//监听 Channel 事件
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();//获取当前SelectionKey
                //区分事件类型
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel acceptChannel = channel.accept();//接收连接
                    acceptChannel.configureBlocking(false);//非阻塞
                    acceptChannel.register(selector, SelectionKey.OP_READ);//注册到Selector并监听READ事件
                    System.out.println("客户端连接成功！");
                } else if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    int read = channel.read(buffer);
                    if (read == -1) {//正常断开连接，返回值为-1
                        selectionKey.cancel();
                        System.out.println("客户端断开连接！");
                    } else {//读取发来数据，向客户端返回一个对应响应
                        buffer.flip();
                        System.out.println("客户端发来数据！" + StandardCharsets.UTF_8.decode(buffer));
                        selectionKey.interestOps(selectionKey.interestOps() + SelectionKey.OP_WRITE);
                        channel.write(StandardCharsets.UTF_8.encode("Hi"));
                        selectionKey.interestOps(selectionKey.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
                selectionKeyIterator.remove();//移除当前SelectionKey
            }
        }
    }

}
