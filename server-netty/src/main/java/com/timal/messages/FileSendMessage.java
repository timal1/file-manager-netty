package com.timal.messages;

import lombok.Data;

@Data
public class FileSendMessage extends AbstractMessage {
    public String filename;
    public int partNumber;
    public int partsCount;
    public byte[] data;
    public String nameServerPCPath;
    public boolean labelRemoveFile;
    public String nameClientPcPath;

    public FileSendMessage(String filename, int partNumber, int partsCount, byte[] data, String nameServerPCPath, String nameClientPcPath, boolean labelRemoveFile) {
        this.filename = filename;
        this.partNumber = partNumber;
        this.partsCount = partsCount;
        this.data = data;
        this.nameServerPCPath = nameServerPCPath;
        this.labelRemoveFile = labelRemoveFile;
        this.nameClientPcPath = nameClientPcPath;
    }
}

