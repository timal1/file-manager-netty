package com.timal.handlers;


import com.timal.manager.AuthService;
import com.timal.manager.ServerHandler;
import com.timal.messages.RegisterMessage;
import io.netty.channel.ChannelHandlerContext;


public class RegisterUserHandler {

    private AuthService authService;

    public RegisterUserHandler(ServerHandler serverHandler) {
        this.authService = serverHandler.getAuthService();
    }

    public void registerHandle(ChannelHandlerContext ctx, Object msg) {
        RegisterMessage regMsg = (RegisterMessage) msg;
        if (regMsg.getNameUser().equals(authService.getNickByLoginPass(regMsg.getLogin(), regMsg.getPassUser()))) {
            ctx.writeAndFlush(new RegisterMessage("none", "", ""));
        } else {
            if (authService.registerNewUser(regMsg.getNameUser(), regMsg.getLogin(), regMsg.getPassUser())) {
                ctx.writeAndFlush(new RegisterMessage("reg", "", ""));
            }
        }
    }
}
