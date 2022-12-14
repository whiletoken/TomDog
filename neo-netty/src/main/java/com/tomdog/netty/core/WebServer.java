package com.tomdog.netty.core;

import com.tomdog.netty.rest.ControllerFactory;
import com.tomdog.netty.rest.controller.ExceptionHandler;
import com.tomdog.netty.rest.interceptor.Interceptor;
import com.tomdog.netty.rest.interceptor.InterceptorRegistry;
import com.tomdog.netty.rest.mapping.ControllerBean;
import com.tomdog.netty.rest.mapping.ControllerMappingRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Web 服务器类
 *
 * @author Leo
 */
@Data
@Slf4j
public final class WebServer {

    /**
     * 监听端口号
     */
    private int port;

    /**
     * Boss线程数
     */
    private int bossThreads = 1;

    /**
     * Worker线程数
     */
    private int workerThreads = 2;

    /**
     * REST控制器所在包名
     */
    private List<Class<?>> controllerBasePackage;

    /**
     * 忽略Url列表（不搜索Mapping）
     */
    private static final List<String> ignoreUrls = new ArrayList<>(16);

    /**
     * 以上处理器
     */
    private static ExceptionHandler exceptionHandler;

    /**
     * Http 最大内容长度，默认为10M。
     */
    private int maxContentLength = 1024 * 1024 * 10;

    public WebServer(int port) {
        this.port = port;
    }

    public void addInterceptor(Interceptor interceptor, String... excludeMappings) {
        try {
            InterceptorRegistry.addInterceptor(interceptor, excludeMappings);
        } catch (Exception e) {
            log.error("Add filter failed is {}", e.getMessage());
        }
    }

    public static List<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public static ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public static void setExceptionHandler(ExceptionHandler handler) {
        exceptionHandler = handler;
    }

    /**
     * 启动服务
     */
    public void start() throws Exception {

        // 注册所有REST Controller
        ControllerFactory controllerFactory = new ControllerFactory();
        controllerFactory.registerController(controllerBasePackage);

        // BossGroup处理nio的Accept事件（TCP连接）
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(bossThreads);

        // Worker处理nio的Read和Write事件（通道的I/O事件）
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workerThreads);

        try {

            // handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行。
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new HandlerInitializer(maxContentLength));

            ChannelFuture f = bootstrap.bind(port).sync();
            log.info("The netty rest server is now ready to accept requests on port {}", port);

            ControllerMappingRegistry controllerMappingRegistry = ControllerMappingRegistry.newInstance();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
