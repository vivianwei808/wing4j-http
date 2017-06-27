package org.wing4j.http.server.impl;

import lombok.Setter;
import org.springframework.stereotype.Service;
import org.wing4j.http.server.metadata.InterfaceChannelDefineMetadata;
import org.wing4j.http.server.InterfaceChannelInfoService;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;

import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 * 默认的通道信息服务
 */
public class DefaultInterfaceChannelInfoServiceImpl implements InterfaceChannelInfoService {
    @Setter
    List<InterfaceChannelDefineMetadata> interfaceChannelDefineMetadatas;
    @Override
    public InterfaceChannelDefineMetadata lookup(String channelNo) {
        for (InterfaceChannelDefineMetadata interfaceChannelDefineMetadata : interfaceChannelDefineMetadatas){
            if(interfaceChannelDefineMetadata.getChannelNo().equals(channelNo)){
                return interfaceChannelDefineMetadata;
            }
        }
        return null;
    }
}
