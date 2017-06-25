package org.wing4j.http.server;

import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;

import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 */
public interface InterfaceSecurityInfoService {
    /**
     * 增加安全元信息
     * @param interfaceSecurityMetadata
     */
    void add(InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 获取接口安全元信息
     * @param service 服务名称
     * @param name 接口名称
     * @return 接口安全元信息
     */
    InterfaceSecurityMetadata lookup(String service, String name);

    /**
     * 获取服务所有接口的安全信息
     * @param service 服务名称
     * @return 安全信息列表
     */
    List<InterfaceSecurityMetadata> list(String service);
}
