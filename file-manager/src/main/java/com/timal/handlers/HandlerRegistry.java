package com.timal.handlers;


import com.timal.UI.Controller;
import com.timal.messages.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private Map<Class<? extends AbstractMessage>, RequestHandler> mapHandlers;

    public HandlerRegistry(Controller controller) {
        this.mapHandlers = new HashMap<>();
        mapHandlers.put(AuthMessage.class, new AuthHandler(controller)::authHandle);
        mapHandlers.put(RegisterMessage.class, new RegisterUserHandler(controller)::regHandle);
        mapHandlers.put(FileSendMessage.class, new FileHandler(controller)::fileGet);
        mapHandlers.put(DelFileRequestMessage.class, new FileHandler(controller)::fileDelete);
        mapHandlers.put(FilesSizeRequestMessage.class, new FilesSizeHandler(controller)::updateFilesSize);
        mapHandlers.put(FileListMessage.class, new FileListHandler(controller)::updateFilesList);
    }

    public RequestHandler getHandler(Class cl) {
        return mapHandlers.get(cl);
    }
}
