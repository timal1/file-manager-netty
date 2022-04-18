package com.timal.manager;

import com.timal.UI.Controller;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class Network implements Runnable {

    private static final String HOST = "localhost";
    private static final int PORT = 8181;

    private Controller controller;
    private Channel currentChannel;
    private CountDownLatch countDownLatch;

    public Network(Controller controller, CountDownLatch countDownLatch) {
        this.controller = controller;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        openConnection();
    }

    public void openConnection() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ClientHandler(controller)
                            );
                        }
                    });
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            currentChannel = channelFuture.sync().channel();
            countDownLatch.countDown();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ChannelFuture send(Object msg) {
        return currentChannel.writeAndFlush(msg);
    }

    public void close() {
        currentChannel.close();
    }
}
