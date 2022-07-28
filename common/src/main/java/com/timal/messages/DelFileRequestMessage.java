package com.timal.messages;

public class DelFileRequestMessage extends AbstractMessage {

    private String nameFile;
    private String namePathServerPC;

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getNamePathServerPC() {
        return namePathServerPC;
    }

    public void setNamePathServerPC(String namePathServerPC) {
        this.namePathServerPC = namePathServerPC;
    }

    public DelFileRequestMessage() {
    }

    public DelFileRequestMessage(String nameFile, String namePathServerPC) {
        this.nameFile = nameFile;
        this.namePathServerPC = namePathServerPC;
    }
}
