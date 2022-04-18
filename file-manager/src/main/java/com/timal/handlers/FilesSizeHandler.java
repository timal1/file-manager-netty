package com.timal.handlers;


import com.timal.UI.Controller;
import com.timal.messages.FileInfoMessage;
import com.timal.messages.FilesSizeRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

import java.util.List;

public class FilesSizeHandler {

    private Controller controller;
    private boolean check;
    private double percentProgressBar;
    private double partsCount;
    private double partNumber;
    private String pathName;

    public FilesSizeHandler(Controller controller) {
        this.controller = controller;
        this.check = false;
    }

    public void updateFilesSize(ChannelHandlerContext ctx, Object msg) {

        FilesSizeRequestMessage filesObj = (FilesSizeRequestMessage) msg;
        double filesSize = (double) filesObj.getFilesSize() / 1024 / 1024 / 1024;
        List<FileInfoMessage> listFiles = filesObj.getListFiles();
        partsCount = filesObj.getPartsCount();
        partNumber = filesObj.getPartNumber();

        if (partsCount != partNumber) {
            percentProgressBar = (1 / partsCount) * partNumber;
            check = true;
        } else { check = false;}

        pathName = controller.getServerPC().getPathNameGetFromServer();

        Platform.runLater(() -> {
            controller.changeLoadBar(filesSize);
            controller.getFileSizeLabel().setText(String.valueOf(filesSize).substring(0, 3));
            controller.getServerPC().updateList(listFiles, pathName);
            controller.setVisibleLoadInfoFile(check);
            controller.changeProgressBar(percentProgressBar);
        });
    }
}
