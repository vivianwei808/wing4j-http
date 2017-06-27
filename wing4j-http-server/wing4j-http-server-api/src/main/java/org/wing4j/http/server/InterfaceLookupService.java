package org.wing4j.http.server;

import org.wing4j.http.server.metadata.InterfaceDefineMetadata;

/**
 * Created by wing4j on 2017/6/25.
 */
public interface InterfaceLookupService {
    /**
     * 查找接口定义元信息
     * @param interfaceName 接口名称
     * @param version 版本号
     * @return
     */
    InterfaceDefineMetadata lookup(String interfaceName, String version);

    /**
     * 扫描接口定义元信息
     */
    void scan();
}
