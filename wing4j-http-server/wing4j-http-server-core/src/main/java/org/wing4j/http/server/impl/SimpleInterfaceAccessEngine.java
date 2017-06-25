package org.wing4j.http.server.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wing4j.http.protocol.code.RspCode;
import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;
import org.wing4j.http.server.*;
import org.wing4j.http.protocol.service.InterfaceSecurityService;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;
import org.wing4j.http.server.metadata.InterfaceServiceDefineMetadata;

/**
 * Created by wing4j on 2017/6/25.
 */
public class SimpleInterfaceAccessEngine implements InterfaceAccessEngine{
    static Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();

    InterfaceLookupService interfaceLookupService;

    InterfaceExecuteService interfaceExecuteService;

    InterfaceSecurityService interfaceSecurityService;

    InterfaceSecurityInfoService interfaceSecurityInfoService;

    InterfaceServiceInfoService interfaceServiceInfoService;
    @Override
    public String execute(String reqJson) {
        Request request = GSON.fromJson(reqJson, Request.class);
        Response response = execute(request);
        String rspJson = GSON.toJson(response);
        return rspJson;
    }

    Response execute(Request request) {
        Response.Builder responseBuilder = Response.builder().service(request.getService()).name(request.getName());
        //检查服务是否定义
        InterfaceDefineMetadata interfaceDefineMetadata = interfaceLookupService.lookup(request.getService(), request.getName(), request.getVersion());
        if(interfaceDefineMetadata == null){//如果服务器没有定义接口服务，则直接返回
            responseBuilder.code(RspCode.INTERFACE_NOT_DEFINE);
            return responseBuilder.build();
        }
        Response response = responseBuilder.build();
        //根据服务名和接口名获取安全元信息
        InterfaceSecurityMetadata interfaceSecurityMetadata = interfaceSecurityInfoService.lookup(request.getService(), request.getName());
        if(interfaceSecurityMetadata == null){
            //TODO
            responseBuilder.code(RspCode.INTERFACE_NOT_DEFINE);
            return responseBuilder.build();
        }
        InterfaceServiceDefineMetadata interfaceServiceDefineMetadata = interfaceServiceInfoService.lookup(request.getService());
        if(interfaceServiceDefineMetadata == null){
            //TODO
            responseBuilder.code(RspCode.INTERFACE_NOT_DEFINE);
            return responseBuilder.build();
        }
        //如果验签优先于解密
        if(interfaceSecurityMetadata.isVerifyFirstDecryptSecond()){
            if(!interfaceSecurityService.verify(response,interfaceServiceDefineMetadata.getSignPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.VERIFY_FAIL);
                return responseBuilder.build();
            }
            if(!interfaceSecurityService.decrypt(response,interfaceServiceDefineMetadata.getCipherPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.DECRYPT_FAIL);
                return responseBuilder.build();
            }
        }else{
            if(!interfaceSecurityService.decrypt(response,interfaceServiceDefineMetadata.getCipherPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.DECRYPT_FAIL);
                return responseBuilder.build();
            }
            if(!interfaceSecurityService.verify(response,interfaceServiceDefineMetadata.getSignPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.VERIFY_FAIL);
                return responseBuilder.build();
            }
        }
        response = interfaceExecuteService.call(request, interfaceDefineMetadata);
        //如果签字优先于加密
        if (interfaceSecurityMetadata.isSignFirstEncryptSecond()){
            if(!interfaceSecurityService.sign(request,interfaceServiceDefineMetadata.getSignPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.SIGN_HAPPENS_ERROR);
                return responseBuilder.build();
            }
            if(!interfaceSecurityService.encrypt(request,interfaceServiceDefineMetadata.getCipherPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.ENCRYPT_HAPPENS_ERROR);
                return responseBuilder.build();
            }
        }else{
            if(!interfaceSecurityService.encrypt(request,interfaceServiceDefineMetadata.getCipherPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.ENCRYPT_HAPPENS_ERROR);
                return responseBuilder.build();
            }
            if(!interfaceSecurityService.sign(request,interfaceServiceDefineMetadata.getSignPassword(), interfaceSecurityMetadata)){
                responseBuilder.code(RspCode.SIGN_HAPPENS_ERROR);
                return responseBuilder.build();
            }
        }
        return response;
    }
}
