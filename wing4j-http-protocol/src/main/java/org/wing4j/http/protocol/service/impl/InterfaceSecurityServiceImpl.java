package org.wing4j.http.protocol.service.impl;

import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;
import org.wing4j.http.protocol.service.InterfaceSecurityService;
import org.wing4j.security.AesUtils;
import org.wing4j.security.SignUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by wing4j on 2017/6/25.
 */
public class InterfaceSecurityServiceImpl implements InterfaceSecurityService {
    @Override
    public boolean encrypt(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata){
        try {
            String cipherText = AesUtils.encrypt(request.getData(), password, "UTF-8");
            request.setData(cipherText);
            request.setCipherType(interfaceSecurityMetadata.getEncryptAlgorithm());
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean encrypt(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            String cipherText = AesUtils.encrypt(response.getData(), password, "UTF-8");
            response.setData(cipherText);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean decrypt(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            String plainText = AesUtils.decrypt(response.getData(), password, "UTF-8");
            response.setData(plainText);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean decrypt(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            String plainText = AesUtils.decrypt(request.getData(), password, "UTF-8");
            request.setData(plainText);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean sign(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            String sign = SignUtils.sign(request.getData(), password, "UTF-8");
            request.setSign(sign);
            request.setSignType(interfaceSecurityMetadata.getSignAlgorithm());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean sign(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            String sign = SignUtils.sign(response.getData(), password, "UTF-8");
            response.setSign(sign);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verify(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            return SignUtils.verify(response.getData(), response.getSign(), password, "UTF-8");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verify(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata) {
        try {
            return SignUtils.verify(request.getData(), request.getSign(), password, "UTF-8");
        } catch (Exception e) {
            return false;
        }
    }

}
