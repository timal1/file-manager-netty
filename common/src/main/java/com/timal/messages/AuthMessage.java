package com.timal.messages;

import java.util.List;

public class AuthMessage extends AbstractMessage {

    private String loginUser;
    private String passUser;
    private List<FileInfoMessage> listFiles;
    private String pathName;

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public List<FileInfoMessage> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<FileInfoMessage> listFiles) {
        this.listFiles = listFiles;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public AuthMessage() {
    }

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
