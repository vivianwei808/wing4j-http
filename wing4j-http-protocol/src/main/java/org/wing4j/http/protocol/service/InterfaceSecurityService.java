package org.wing4j.http.protocol.service;

import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;

/**
 * Created by wing4j on 2017/6/25.
 * 接口安全服务
 */
public interface InterfaceSecurityService {
    /**
     * 进行请求加密
     * @param request 请求
     * @return 处理成功返回真
     */
    boolean encrypt(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行应答加密
     * @param response 应答
     * @return 处理成功返回真
     */
    boolean encrypt(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行应答解密
     * @param response
     * @return
     */
    boolean decrypt(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行请求解密
     * @param request
     * @return
     */
    boolean decrypt(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行请求签字
     * @param request
     * @return
     */
    boolean sign(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行应答签字
     * @param response
     * @return
     */
    boolean sign(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行应答验签
     * @param response
     * @return
     */
    boolean verify(Response response, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
    /**
     * 进行请求验签
     * @param request
     * @return
     */
    boolean verify(Request request, String password, InterfaceSecurityMetadata interfaceSecurityMetadata);
}
