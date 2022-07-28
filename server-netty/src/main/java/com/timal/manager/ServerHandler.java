package com.timal.manager;

import com.timal.handlers.HandlerRegistry;
import com.timal.handlers.RequestHandler;
import com.timal.messages.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Log4j2
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final String pathMain = "server-netty\\D\\";

    private List<ChannelsInfo> clients = new ArrayList<>();
    private AuthService authService;
    private ExecutorService executorService;
    private FilesInformService filesInformService;
    private FileListMessage fileListMessage;
    private List<FileInfoMessage> fileInfoMessages;
    private String pathNameUser;
    private FileOutputStream fos;
    private boolean append;
    private HandlerRegistry handlerRegistry;
    private String userName;

    public ServerHandler(AuthService authService) {
        this.authService = authService;
        this.executorService = Executors.newSingleThreadExecutor();
        this.filesInformService = new FilesInformService();
        this.handlerRegistry = new HandlerRegistry(this);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("User " + userName + " connected..." + ctx);
        filesInformService = new FilesInformService();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("User " + userName + " disconnected..." + ctx);
    }

    @Override
    @SneakyThrows
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        RequestHandler handler = handlerRegistry.getHandler(msg.getClass());
        handler.handle(ctx, msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        authService.stop();
        executorService.shutdown();
        cause.printStackTrace();
        ctx.close();
        log.info("Connect close");
    }

    public boolean isNickBusy(String nickName) {
        for (ChannelsInfo client : clients) {
            if (client.getClientName().equals(nickName)) {
                return true;
            }
        }
        return false;
    }

    public String isChanelBusy(ChannelHandlerContext ctx) {

        for (ChannelsInfo client : clients) {
            if (client.getChannel().equals(ctx.channel())) {
                return client.getClientName();
            }
        }
        return null;
    }
}




