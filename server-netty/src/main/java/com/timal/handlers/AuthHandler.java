package com.timal.handlers;

import com.timal.manager.AuthService;
import com.timal.manager.ChannelsInfo;
import com.timal.manager.FilesInformService;
import com.timal.manager.ServerHandler;
import com.timal.messages.AuthMessage;
import com.timal.messages.FilesSizeRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class AuthHandler {

    private ServerHandler serverHandler;
    private AuthService authService;
    private FilesInformService filesInformService;
    private String pathNameUser;


    public AuthHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        this.authService = serverHandler.getAuthService();
        this.filesInformService = serverHandler.getFilesInformService();
    }

    public void authHandle(ChannelHandlerContext ctx, Object msg) {
        String name = ((AuthMessage) msg).getLoginUser();
        String pass = ((AuthMessage) msg).getPassUser();
        String userName = authService.getNickByLoginPass(name, pass);
        if (userName != null & !serverHandler.isNickBusy(userName)) {
            serverHandler.getClients().add(new ChannelsInfo(ctx.channel(), userName));
            pathNameUser = filesInformService.getPathMain() + userName;
            try {
                AuthMessage authMessage = new AuthMessage(userName,
                        filesInformService.getListFiles(pathNameUser), pathNameUser);
                ctx.writeAndFlush(authMessage);

                FilesSizeRequestMessage filesSizeRequestMessage = new FilesSizeRequestMessage(filesInformService.getFilesSize(userName),
                        filesInformService.getListFiles(pathNameUser), pathNameUser);
                ctx.writeAndFlush(filesSizeRequestMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("Authorization was completed successfully and a list of files was sent to the client");
        } else {
            ctx.writeAndFlush(new AuthMessage("none", null, ""));
            log.info("Authorization was not completed.");
        }
    }
}
