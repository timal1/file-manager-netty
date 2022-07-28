package com.timal.messages;

public class FileRequestMessage extends AbstractMessage {

    private String nameServerPCPath;
    private boolean labelRemoveFile;
    private String fileName;
    private String nameClientPcPath;

    public String getNameServerPCPath() {
        return nameServerPCPath;
    }

    public void setNameServerPCPath(String nameServerPCPath) {
        this.nameServerPCPath = nameServerPCPath;
    }

    public boolean isLabelRemoveFile() {
        return labelRemoveFile;
    }

    public void setLabelRemoveFile(boolean labelRemoveFile) {
        this.labelRemoveFile = labelRemoveFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNameClientPcPath() {
        return nameClientPcPath;
    }

    public void setNameClientPcPath(String nameClientPcPath) {
        this.nameClientPcPath = nameClientPcPath;
    }

    public FileRequestMessage() {
    }

    public FileRequestMessage(String fileName, String nameServerPCPath, String nameClientPcPath, boolean labelRemoveFile) {
        this.nameServerPCPath = nameServerPCPath;
        this.labelRemoveFile = labelRemoveFile;
        this.fileName = fileName;
        this.nameClientPcPath = nameClientPcPath;
    }
}
