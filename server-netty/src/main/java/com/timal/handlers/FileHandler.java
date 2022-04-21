package com.timal.handlers;


import com.timal.manager.FilesInformService;
import com.timal.manager.ServerHandler;
import com.timal.messages.DelFileRequestMessage;
import com.timal.messages.FileRequestMessage;
import com.timal.messages.FileSendMessage;
import com.timal.messages.FilesSizeRequestMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

@Log4j2
public class FileHandler {

    private ServerHandler serverHandler;
    private FilesInformService filesInformService;
    private FileOutputStream fileOutputStream;
    private ExecutorService executorService;
    private boolean append;

    public FileHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        this.filesInformService = serverHandler.getFilesInformService();
        this.executorService = serverHandler.getExecutorService();
    }

    public void fileGet(ChannelHandlerContext ctx, Object msg) {

        try {
            String userName = serverHandler.isChanelBusy(ctx);
            if (userName == null) {
                log.error("The user is not logged in");
                return;
            }
            FileSendMessage fileSendMessage = (FileSendMessage) msg;

            if (fileSendMessage.partNumber == 1) {
                append = false;
                fileOutputStream = null;
                fileOutputStream = new FileOutputStream(fileSendMessage.nameServerPCPath + "\\"
                        + fileSendMessage.filename, append);
            } else {
                append = true;
            }

            fileOutputStream.write(fileSendMessage.data);

            if (fileSendMessage.partNumber == fileSendMessage.partsCount) {
                fileOutputStream.close();
                append = false;
                log.info("File full get");

                if (fileSendMessage.labelRemoveFile) {
                    ctx.writeAndFlush(new DelFileRequestMessage(fileSendMessage.filename,
                            fileSendMessage.nameClientPcPath));
                }
            }

            FilesSizeRequestMessage filesSizeRequestMessage = new FilesSizeRequestMessage(
                    filesInformService.getFilesSize(userName),
                    filesInformService.getListFiles(fileSendMessage.nameServerPCPath),
                    fileSendMessage.partNumber, fileSendMessage.partsCount, fileSendMessage.nameServerPCPath);
            ctx.writeAndFlush(filesSizeRequestMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOut(ChannelHandlerContext ctx, Object msg) {

        executorService.execute(() -> {
            try {
                String userName = serverHandler.isChanelBusy(ctx);

                if (userName == null) {
                    log.error("The user is not logged in");
                    return;
                }

                FileRequestMessage frMsg = (FileRequestMessage) msg;
                String nameServerPCPath = frMsg.getNameServerPCPath();
                String nameClientPcPath = frMsg.getNameClientPcPath();
                boolean labelRemoveFile = frMsg.isLabelRemoveFile();
                String nameFile = frMsg.getFileName();
                File file = new File(nameServerPCPath + "\\" + nameFile);
                int bufSize = 1024 * 1024 * 10;
                int partsCount = (int) (file.length() / bufSize);

                if (file.length() % bufSize != 0) {
                    partsCount++;
                }

                FileSendMessage fmOut = new FileSendMessage(file.getName(), -1, partsCount,
                        new byte[bufSize], nameServerPCPath, nameClientPcPath, labelRemoveFile);
                FileInputStream in = new FileInputStream(file);

                for (int i = 0; i < partsCount; i++) {
                    int readBytes = in.read(fmOut.data);
                    fmOut.partNumber = i + 1;

                    if (readBytes < bufSize) {
                        fmOut.data = Arrays.copyOfRange(fmOut.data, 0, readBytes);
                    }

                    ChannelFuture f = ctx.writeAndFlush(fmOut);
                    f.sync();
                }

                in.close();
                ChannelFuture future = ctx.writeAndFlush(new FilesSizeRequestMessage(filesInformService.getFilesSize(userName),
                        filesInformService.getListFiles(nameServerPCPath), frMsg.getNameServerPCPath()));
                future.sync();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteFile(ChannelHandlerContext ctx, Object msg) {

        String userName = serverHandler.isChanelBusy(ctx);

        if (userName == null) {
            log.error("The user is not logged in");
            return;
        }
        
        DelFileRequestMessage deleteFileRequest = (DelFileRequestMessage) msg;
        String nameDelFile = deleteFileRequest.getNameFile();
        String pathFileDelete = deleteFileRequest.getNamePathServerPC();
        try {
            Files.delete(Paths.get(pathFileDelete, nameDelFile));
            log.info("User " + userName + " remove file " + nameDelFile);
            ctx.writeAndFlush(new FilesSizeRequestMessage(filesInformService.getFilesSize(userName),
                    filesInformService.getListFiles(pathFileDelete), pathFileDelete));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
