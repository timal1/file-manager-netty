package com.timal.handlers;

import com.timal.manager.FilesInformService;
import com.timal.manager.ServerHandler;
import com.timal.messages.FileInfoMessage;
import com.timal.messages.FileListMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class FileListHandler {

    private ServerHandler serverHandler;
    private FilesInformService filesInformService;
    private FileListMessage fileListMessage;
    private List<FileInfoMessage> fileInfoMessages;

    public FileListHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        this.filesInformService = serverHandler.getFilesInformService();
    }

    @SneakyThrows
    public void updateFilesList(ChannelHandlerContext ctx, Object msg) {

        String userName = serverHandler.isChanelBusy(ctx);
        if (userName == null) {
            log.error("The user is not logged in");
            return;
        }

        fileListMessage = (FileListMessage) msg;
        String pathName = fileListMessage.getPathName();

        if (!pathName.equals("server-netty\\D")) {
            fileInfoMessages = filesInformService.getListFiles(pathName);
            fileListMessage = new FileListMessage(filesInformService.getListFiles(pathName), pathName);
            ctx.writeAndFlush(fileListMessage);
        }
    }

}
