package org.wing4j.http.server;

import org.wing4j.http.server.metadata.InterfaceChannelDefineMetadata;

/**
 * Created by wing4j on 2017/6/25.
 * 通道信息服务
 */
public interface InterfaceChannelInfoService {
    /**
     * 查询通道元信息
     */
    InterfaceChannelDefineMetadata lookup(String channelNo);
}
