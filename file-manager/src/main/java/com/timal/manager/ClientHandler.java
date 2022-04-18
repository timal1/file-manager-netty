package com.timal.manager;


import com.timal.UI.Controller;
import com.timal.handlers.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClientHandler extends SimpleChannelInboundHandler {

    private HandlerRegistry handlerRegistry;

    public ClientHandler(Controller controller) {
        this.handlerRegistry = new HandlerRegistry(controller);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        RequestHandler handler = handlerRegistry.getHandler(msg.getClass());
        handler.handle(ctx, msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
