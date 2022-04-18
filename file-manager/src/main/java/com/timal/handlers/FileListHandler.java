package com.timal.handlers;

import com.timal.UI.Controller;
import com.timal.messages.FileInfoMessage;
import com.timal.messages.FileListMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;

import java.util.List;

public class FileListHandler {

    private Controller controller;
    private List<FileInfoMessage> fileInfoMessages;
    private String pathName;


    public FileListHandler(Controller controller) {
        this.controller = controller;
    }

    @SneakyThrows
    public void updateFilesList(ChannelHandlerContext ctx, Object msg) {
        FileListMessage fileListMessage = (FileListMessage) msg;
        fileInfoMessages = fileListMessage.getListFiles();
        pathName = fileListMessage.getPathName();
        controller.getServerPC().updateList(fileInfoMessages, pathName);
    }

}
