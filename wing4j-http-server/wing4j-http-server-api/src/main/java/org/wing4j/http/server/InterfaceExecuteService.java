package org.wing4j.http.server;

import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;

/**
 * Created by wing4j on 2017/6/25.
 * 接口执行服务
 */
public interface InterfaceExecuteService {
    /**
     * 调用接口服务定义类型
     * @param request
     * @param interfaceDefineMetadata
     * @return
     */
    Response call(Request request, InterfaceDefineMetadata interfaceDefineMetadata);
}
