package org.wing4j.http.server.impl;

import org.springframework.stereotype.Service;
import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.server.InterfaceSecurityInfoService;

import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 */
@Service
public class SimpleInterfaceSecurityInfoServiceImpl implements InterfaceSecurityInfoService{

    @Override
    public void add(InterfaceSecurityMetadata interfaceSecurityMetadata) {

    }

    @Override
    public InterfaceSecurityMetadata lookup(String service, String name) {
        return null;
    }

    @Override
    public List<InterfaceSecurityMetadata> list(String service) {
        return null;
    }
}
