package org.wing4j.http.service;

import org.wing4j.http.annotation.HttpService;
import org.wing4j.http.protocol.domains.FetchInterfaceRequest;
import org.wing4j.http.protocol.domains.FetchInterfaceResponse;
import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.service.InterfaceService;
import org.wing4j.http.server.InterfaceSecurityInfoService;

import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 */
@HttpService(service = "interfaceService")
public class InterfaceServiceImpl implements InterfaceService {
    InterfaceSecurityInfoService interfaceSecurityInfoService;
    @Override
    public FetchInterfaceResponse fetchInterfaceDefine(FetchInterfaceRequest request) {
        List<InterfaceSecurityMetadata> interfaceSecurityMetadatas = interfaceSecurityInfoService.list(request.getService());
        for (InterfaceSecurityMetadata interfaceSecurityMetadata : interfaceSecurityMetadatas){
            String signAlgorithm = interfaceSecurityMetadata.getSignAlgorithm();
            String verifyAlgorithm = interfaceSecurityMetadata.getVerifyAlgorithm();
            String encryptAlgorithm = interfaceSecurityMetadata.getEncryptAlgorithm();
            String decryptAlgorithm = interfaceSecurityMetadata.getDecryptAlgorithm();
            boolean verifyFirstDecryptSecond = interfaceSecurityMetadata.isVerifyFirstDecryptSecond();
            boolean signFirstEncryptSecond = interfaceSecurityMetadata.isSignFirstEncryptSecond();
            interfaceSecurityMetadata.setSignAlgorithm(verifyAlgorithm);
            interfaceSecurityMetadata.setVerifyAlgorithm(signAlgorithm);
            interfaceSecurityMetadata.setEncryptAlgorithm(decryptAlgorithm);
            interfaceSecurityMetadata.setDecryptAlgorithm(encryptAlgorithm);
            interfaceSecurityMetadata.setSignFirstEncryptSecond(!verifyFirstDecryptSecond);
            interfaceSecurityMetadata.setVerifyFirstDecryptSecond(!signFirstEncryptSecond);
        }
        FetchInterfaceResponse response = FetchInterfaceResponse.builder().channeNo(request.getService()).build();
        response.getInterfaces().addAll(interfaceSecurityMetadatas);
        return response;
    }
}
