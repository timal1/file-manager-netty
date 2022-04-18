package com.timal.messages;

import lombok.Data;

@Data
public class FileRequestMessage extends AbstractMessage {

    private String nameServerPCPath;
    private boolean labelRemoveFile;
    private String fileName;
    private String nameClientPcPath;

    public FileRequestMessage(String fileName, String nameServerPCPath, String nameClientPcPath, boolean labelRemoveFile) {
        this.nameServerPCPath = nameServerPCPath;
        this.labelRemoveFile = labelRemoveFile;
        this.fileName = fileName;
        this.nameClientPcPath = nameClientPcPath;
    }
}
