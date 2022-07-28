package com.timal.messages;

import lombok.Data;

import java.util.List;

@Data
public class FileListMessage extends AbstractMessage {

    private List<FileInfoMessage> listFiles;
    private String pathName;

    public FileListMessage() {
    }

    public FileListMessage(String pathName) {
        this.pathName = pathName;
    }

    public FileListMessage(List<FileInfoMessage> listFiles, String pathName) {
        this.listFiles = listFiles;
        this.pathName = pathName;
    }
}
