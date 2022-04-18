package com.timal.manager;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class ChannelsInfo {
    private Channel channel;
    private String clientName;

    public ChannelsInfo(Channel channel, String clientName) {
        this.channel = channel;
        this.clientName = clientName;
    }
}
