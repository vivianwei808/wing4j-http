package org.wing4j.http.server.impl;

import org.springframework.stereotype.Service;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;
import org.wing4j.http.server.InterfaceLookupService;

/**
 * Created by wing4j on 2017/6/25.
 * 默认的接口发现服务
 */
@Service
public class DefaultInterfaceLookupServiceImpl implements InterfaceLookupService {
    @Override
    public InterfaceDefineMetadata lookup(String serviceName, String interfaceName, String version) {
        return null;
    }
}
