package com.timal.messages;

import lombok.Data;

import java.util.List;

@Data
public class AuthMessage extends AbstractMessage {

    private String loginUser;
    private String passUser;
    private List<FileInfoMessage> listFiles;
    private String pathName;

    public AuthMessage(String loginUser, String passUser) {
        this.loginUser = loginUser;
        this.passUser = passUser;
    }

    public AuthMessage(String loginUser, List<FileInfoMessage> listFiles, String pathName) {
        this.loginUser = loginUser;
        this.listFiles = listFiles;
        this.pathName = pathName;
    }
}
