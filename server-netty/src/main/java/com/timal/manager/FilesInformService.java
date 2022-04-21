package com.timal.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.timal.messages.FileInfoMessage;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FilesInformService {

    private String pathMain = "server-netty\\D\\";

    public List<FileInfoMessage> getListFiles(String pathName) throws IOException {
        if (pathName != null) {
            Path path = Paths.get(pathName);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            return Files.list(path).map(FileInfoMessage::new).collect(Collectors.toList());
        }
        return null;
    }

    public long getFilesSize(String nameUser) throws IOException {
        if (nameUser != null) {
            Path path = Paths.get(pathMain, nameUser);

            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            long size = Files.list(path)
                    .map((p) -> p.toFile()
                            .length())
                    .reduce((s1, s2) -> s1 + s2)
                    .orElse(Long.valueOf(0));
            List<FileInfoMessage> fileInfoMessages = Files.list(path).map(FileInfoMessage::new).collect(Collectors.toList());

            for (FileInfoMessage fileInfoMessage : fileInfoMessages) {
                if (fileInfoMessage.getType().equals(FileInfoMessage.FileType.DIRECTORY)) {
                    Path pathFile = Paths.get(pathMain, nameUser, fileInfoMessage.getFilename());
                    size += Files.list(pathFile)
                            .map((p) -> p.toFile()
                                    .length())
                            .reduce((s1, s2) -> s1 + s2)
                            .orElse(Long.valueOf(0));
                }
            }
            return size;
        }
        return 0;
    }
}
