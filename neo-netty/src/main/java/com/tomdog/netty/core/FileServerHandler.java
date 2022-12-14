package com.tomdog.netty.core;

import cn.hutool.core.io.IoUtil;
import com.tomdog.netty.util.DonkeyHttpUtil;
import com.tomdog.netty.util.ResourcesService;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.internal.StringUtil;

import java.io.File;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * 静态资源处理
 * <a href="http://localhost:8081/static/index.html">...</a>
 */
public class FileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        // 向下传递计数器
        channelHandlerContext.fireChannelRead(request.retain());
        String uri = request.uri();
        if (!uri.endsWith(".js") && !uri.endsWith(".css") && !uri.endsWith(".html")) {
            return;
        }
        HttpResponse response;
        RandomAccessFile randomAccessFile = null;
        try {
            // 状态为1xx的话，继续请求
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(channelHandlerContext);
            }
            int index = uri.lastIndexOf("/") + 1;
            String filename = uri.substring(index);
            uri = uri.substring(0, index - 1);
            String path = ResourcesService.getInstance().getPath(uri);
            // 文件路径不存在
            if (StringUtil.isNullOrEmpty(path)) {
                DonkeyHttpUtil.writeResponse(request, NOT_FOUND, channelHandlerContext);
                return;
            }
            String fullPath = path + "/" + filename;
            File file = new File(fullPath);
            if (file.exists()) {
                randomAccessFile = new RandomAccessFile(file, "r");
            } else {
                // 文件不存在
                DonkeyHttpUtil.writeResponse(request, NOT_FOUND, channelHandlerContext);
                return;
            }
            long fileLength = randomAccessFile.length();
            response = new DefaultHttpResponse(request.protocolVersion(), OK);

            setContentType(response, file);
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            channelHandlerContext.write(response);

            ChannelFuture sendFileFuture = channelHandlerContext
                    .write(new ChunkedNioFile(randomAccessFile.getChannel()), channelHandlerContext.newProgressivePromise());
            // 写入文件尾部
            sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationProgressed(ChannelProgressiveFuture future,
                                                long progress, long total) {
                    if (total < 0) { // total unknown
                        System.out.println("Transfer progress: " + progress);
                    } else {
                        System.out.println("Transfer progress: " + progress + " / " + total);
                    }
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture future) {
                    System.out.println("Transfer complete.");
                }
            });
            ChannelFuture lastContentFuture = channelHandlerContext
                    .writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } finally {
            IoUtil.close(randomAccessFile);
        }
    }

    private void setContentType(HttpResponse response, File file) {
        // MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        if (file.getName().endsWith(".js")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-javascript");
        } else if (file.getName().endsWith(".css")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css; charset=UTF-8");
        } else if (file.getName().endsWith(".html")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}