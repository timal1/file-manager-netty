package com.timal.messages;

import lombok.Data;

@Data
public class DelFileRequestMessage extends AbstractMessage {

    private String nameFile;
    private String namePathServerPC;

    public DelFileRequestMessage(String nameFile, String namePathServerPC) {
        this.nameFile = nameFile;
        this.namePathServerPC = namePathServerPC;
    }
}
