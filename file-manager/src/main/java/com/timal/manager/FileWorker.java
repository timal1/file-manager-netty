package com.timal.manager;

import com.timal.UI.Controller;
import com.timal.messages.FileSendMessage;
import com.timal.messages.FilesSizeRequestMessage;

import io.netty.channel.ChannelFuture;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Log4j2
public class FileWorker {


    private long lastLoadSizeFiles;
    private Network network;
    private ChangeInterface changeInterface;
    private ExecutorService executorService;

    public FileWorker() {

        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void working(File file, ChangeInterface changeInterface, boolean reWrite,
                        String nameServerPCPath, String nameClientPcPath, boolean labelRemoveFile) {
        this.network = Controller.getNetwork();
        this.changeInterface = changeInterface;
        if (reWrite) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Файл будет перезаписан.");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {
                return;
            } else if (option.get() == ButtonType.OK) {

                fileOut(file, nameServerPCPath, nameClientPcPath, labelRemoveFile);
                return;
            } else if (option.get() == ButtonType.CANCEL) {
                return;
            }
        }
        if (lastLoadSizeFiles + file.length() > Controller.CAPACITY_CLOUD_IN_GB * 1024 * 1024 * 1024) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Нехватает места в облаке для сохранения файла.");
            alert.show();
            return;
        }

        fileOut(file, nameServerPCPath, nameClientPcPath, labelRemoveFile);
    }

    public void fileOut(File file, String nameServerPCPath, String nameClientPcPath,
                        boolean labelRemoveFile) {
        executorService.execute(() -> {
            try {
                int bufSize = 1024 * 1024 * 10;
                int partsCount = (int) (file.length() / bufSize);
                if (file.length() % bufSize != 0) {
                    partsCount++;
                }
                Platform.runLater(() -> {
                    changeInterface.call(true);
                });
                FileSendMessage fileSendMessageOut = new FileSendMessage(file.getName(), -1, partsCount,
                        new byte[bufSize], nameServerPCPath, nameClientPcPath, labelRemoveFile);
                FileInputStream in = new FileInputStream(file);
                for (int i = 0; i < partsCount; i++) {
                    int readBytes = in.read(fileSendMessageOut.data);
                    fileSendMessageOut.partNumber = i + 1;
                    if (readBytes < bufSize) {
                        fileSendMessageOut.data = Arrays.copyOfRange(fileSendMessageOut.data, 0, readBytes);
                    }
                    ChannelFuture f = network.send(fileSendMessageOut);
                    f.sync();

                }
                log.info("Фаил: " + file.getName() + " отправлен полностью");
                ChannelFuture f = network.send(new FilesSizeRequestMessage(1, nameServerPCPath));
                f.sync();
                in.close();
                Platform.runLater(() -> {
                    changeInterface.call(false);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        executorService.shutdown();
    }
}
