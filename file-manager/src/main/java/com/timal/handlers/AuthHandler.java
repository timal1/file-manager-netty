package com.timal.handlers;

import com.timal.UI.Controller;
import com.timal.messages.AuthMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import lombok.SneakyThrows;

import java.io.IOException;

public class AuthHandler {

    private Controller controller;

    public AuthHandler(Controller controller) {
        this.controller = controller;
    }

    @SneakyThrows
    public void authHandle(ChannelHandlerContext ctx, Object msg) {
        AuthMessage authMsg = (AuthMessage) msg;
        if (!authMsg.getLoginUser().equals("none")) {
                openFileManagerWindow(authMsg);
        } else {
            authNo();
        }
    }

    public void openFileManagerWindow(AuthMessage authMsg) throws IOException {
        controller.getServerPC().updateList(authMsg.getListFiles(), authMsg.getPathName());
        controller.changeStageToCloud();
    }

    public void authNo() {
        Platform.runLater(() -> {
            controller.getAuthMessage().setVisible(true);
            controller.getAuthMessage().setText("Авторизация не пройдена!");
        });
    }
}
