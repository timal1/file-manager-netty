package com.timal.handlers;


import com.timal.UI.Controller;
import com.timal.messages.RegisterMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

public class RegisterUserHandler {

    private Controller controller;

    public RegisterUserHandler(Controller controller) {
        this.controller =controller;
    }

    public void regHandle(ChannelHandlerContext ctx, Object msg) {
        RegisterMessage regUserRequest = (RegisterMessage) msg;
        if (regUserRequest.getNameUser().equals("none")) {
            Platform.runLater(() -> {
                controller.getRegMessage().setText("Регистрация не пройдена!");
                controller.getRegMessage().setVisible(true);
            });
        } else {
            Platform.runLater(() -> {
                controller.changeStageToAuth();
                controller.getAuthMessage().setText("Регистрация пройдена!");
                controller.getAuthMessage().setVisible(true);
            });
        }
    }
}
