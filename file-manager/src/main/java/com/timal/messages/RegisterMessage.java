package com.timal.messages;

import lombok.Data;

@Data
public class RegisterMessage extends AbstractMessage {

    private String nameUser;
    private String login;
    private String passUser;

    public RegisterMessage(String nameUser, String login, String passUser) {
        this.nameUser = nameUser;
        this.login = login;
        this.passUser = passUser;
    }
}

