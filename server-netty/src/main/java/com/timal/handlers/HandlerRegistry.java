package com.timal.handlers;


import com.timal.manager.ServerHandler;
import com.timal.messages.*;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;


@Data
public class HandlerRegistry {

    private Map<Class<? extends AbstractMessage>, RequestHandler> mapHandlers;

    public HandlerRegistry(ServerHandler serverHandler) {
        this.mapHandlers = new HashMap<>();
        mapHandlers.put(AuthMessage.class, new AuthHandler(serverHandler)::authHandle);
        mapHandlers.put(RegisterMessage.class, new RegisterUserHandler(serverHandler)::registerHandle);
        mapHandlers.put(FileRequestMessage.class, new FileHandler(serverHandler)::fileOut);
        mapHandlers.put(DelFileRequestMessage.class, new FileHandler(serverHandler)::deleteFile);
        mapHandlers.put(FileSendMessage.class, new FileHandler(serverHandler)::fileGet);
        mapHandlers.put(FilesSizeRequestMessage.class, new FilesListRequestHandler(serverHandler)::filesListHandle);
        mapHandlers.put(FileListMessage.class, new FileListHandler(serverHandler)::updateFilesList);
    }

    public RequestHandler getHandler(Class cl) {
        return mapHandlers.get(cl);
    }
}
