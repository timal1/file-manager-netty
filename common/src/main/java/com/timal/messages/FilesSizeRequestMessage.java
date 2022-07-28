package com.timal.messages;

import lombok.Data;

import java.util.List;

@Data
public class FilesSizeRequestMessage extends AbstractMessage {

    private long filesSize;
    private List<FileInfoMessage> listFiles;
    private int partNumber;
    private int partsCount;
    private String pathServerPcName;

    public FilesSizeRequestMessage(long filesSize, String pathServerPcName) {
        this.filesSize = filesSize;
        this.pathServerPcName = pathServerPcName;
    }

    public FilesSizeRequestMessage(long filesSize, List<FileInfoMessage> listFiles, String pathServerPcName) {
        this.filesSize = filesSize;
        this.listFiles = listFiles;
        this.pathServerPcName = pathServerPcName;
    }

    public FilesSizeRequestMessage(long filesSize, List<FileInfoMessage> listFiles, int partNumber, int partsCount, String pathServerPcName) {
        this.filesSize = filesSize;
        this.listFiles = listFiles;
        this.partNumber = partNumber;
        this.partsCount = partsCount;
        this.pathServerPcName = pathServerPcName;
    }
}
