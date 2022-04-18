package com.timal.manager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServerApp {

    private static final int PORT = 8181;

    private static AuthService authService = new AuthServiceBD();

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null))); // Максимальная длина
                            ch.pipeline().addLast(new ServerHandler(authService));
                        }
                    });

            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
