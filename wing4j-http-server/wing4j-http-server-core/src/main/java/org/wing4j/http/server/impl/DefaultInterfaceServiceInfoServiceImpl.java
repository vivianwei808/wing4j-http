package org.wing4j.http.server.impl;

import org.springframework.stereotype.Service;
import org.wing4j.http.server.metadata.InterfaceServiceDefineMetadata;
import org.wing4j.http.server.InterfaceServiceInfoService;

/**
 * Created by wing4j on 2017/6/25.
 * 默认的服务信息服务
 */
@Service
public class DefaultInterfaceServiceInfoServiceImpl implements InterfaceServiceInfoService {
    @Override
    public InterfaceServiceDefineMetadata lookup(String service) {
        return null;
    }
}
