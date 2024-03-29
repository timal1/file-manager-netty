package com.timal.handlers;


import com.timal.manager.FilesInformService;
import com.timal.manager.ServerHandler;
import com.timal.messages.FilesSizeRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class FilesListRequestHandler {

    private ServerHandler serverHandler;
    private FilesInformService filesInformService;

    public FilesListRequestHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        this.filesInformService = serverHandler.getFilesInformService();
    }

    @SneakyThrows
    public void filesListHandle(ChannelHandlerContext ctx, Object msg) {
        FilesSizeRequestMessage filesSizeRequestMessageIn = (FilesSizeRequestMessage)msg;
        String userName = serverHandler.isChanelBusy(ctx);

        if (userName == null) {
            log.error("The user is not logged in");
            return;
        }

        FilesSizeRequestMessage filesSizeRequestMessageOut =
                new FilesSizeRequestMessage(filesInformService.getFilesSize(userName),
                        filesInformService.getListFiles(filesSizeRequestMessageIn.getPathServerPcName()),
                        filesSizeRequestMessageIn.getPathServerPcName());
            ctx.writeAndFlush(filesSizeRequestMessageOut);
            log.info("At the request of the user " + userName + " list of files is sent.");

    }
}
