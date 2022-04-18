package com.timal.handlers;

import com.timal.UI.Controller;
import com.timal.messages.DelFileRequestMessage;
import com.timal.messages.FileSendMessage;
import com.timal.messages.FilesSizeRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.nio.file.Paths;

@Log4j2
public class FileHandler {

    private Controller controller;
    private FileOutputStream fileOutputStream;
    private boolean append;

    public FileHandler(Controller controller) {
        this.controller = controller;
        this.fileOutputStream = null;
    }

    @SneakyThrows
    public void fileGet(ChannelHandlerContext ctx, Object msg) {
        FileSendMessage fileSendMessage = (FileSendMessage) msg;

        if (fileSendMessage.partNumber == 1) {
            append = false;
            fileOutputStream = null;
            fileOutputStream = new FileOutputStream(fileSendMessage.nameClientPcPath + "\\"
                    + fileSendMessage.filename, false);
        } else {
            append = true;
        }
        Platform.runLater(() -> {
            controller.setVisibleLoadInfoFile(true);
            controller.changeProgressBar((double) fileSendMessage.partNumber
                    * ((double) 1 / fileSendMessage.partsCount));
        });

        fileOutputStream.write(fileSendMessage.data);
        if (fileSendMessage.partNumber == fileSendMessage.partsCount) {
            fileOutputStream.close();
            append = false;
            log.info("файл: " + fileSendMessage.filename + " полностью получен с сервера");
            if (fileSendMessage.labelRemoveFile) {
                ctx.writeAndFlush(new DelFileRequestMessage(fileSendMessage.filename,
                        fileSendMessage.nameServerPCPath));
            }
            ctx.writeAndFlush(new FilesSizeRequestMessage(1, fileSendMessage.nameServerPCPath));
            Platform.runLater(() -> {
                controller.setVisibleLoadInfoFile(false);
                controller.getClientPC().updateList(Paths.get(fileSendMessage.nameClientPcPath));
            });
        }
    }

    public void fileDelete(ChannelHandlerContext ctx, Object msg) {
        DelFileRequestMessage delFileRequestmessage = (DelFileRequestMessage) msg;
        String clientPCPath = delFileRequestmessage.getNamePathServerPC();
        String nameDelFile = delFileRequestmessage.getNameFile();
        controller.deleteFile(clientPCPath, nameDelFile);
    }

}
