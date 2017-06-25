package org.wing4j.http.server;

import org.wing4j.http.server.metadata.InterfaceServiceDefineMetadata;

/**
 * Created by wing4j on 2017/6/25.
 * 接口服务信息服务
 */
public interface InterfaceServiceInfoService {
    /**
     * 查询接口服务元信息
     */
    InterfaceServiceDefineMetadata lookup(String service);
}
