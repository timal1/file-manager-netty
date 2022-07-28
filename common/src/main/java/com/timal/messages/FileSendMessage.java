package com.timal.messages;

public class FileSendMessage extends AbstractMessage {
    public String filename;
    public int partNumber;
    public int partsCount;
    public byte[] data;
    public String nameServerPCPath;
    public boolean labelRemoveFile;
    public String nameClientPcPath;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(int partsCount) {
        this.partsCount = partsCount;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

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

    public String getNameClientPcPath() {
        return nameClientPcPath;
    }

    public void setNameClientPcPath(String nameClientPcPath) {
        this.nameClientPcPath = nameClientPcPath;
    }

    public FileSendMessage() {
    }

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

