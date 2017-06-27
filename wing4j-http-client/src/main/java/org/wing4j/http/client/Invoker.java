/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wing4j.http.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wing4j.doc.api.annotation.ApidocInterface;
import org.wing4j.doc.api.annotation.ApidocService;
import org.wing4j.http.protocol.annotation.RequestBody;
import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;
import org.wing4j.http.protocol.service.InterfaceSecurityService;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wing4j on 2017/6/25.
 */
public class Invoker implements InvocationHandler {
    static Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();
    ServiceContext ctx;
    Map<String, InterfaceSecurityMetadata> interfaceDescInfos;

    InterfaceSecurityService interfaceSecurityService;

    public Invoker(ServiceContext ctx, Map<String, InterfaceSecurityMetadata> interfaceDescInfos) {
        this.ctx = ctx;
        this.interfaceDescInfos = interfaceDescInfos;
    }

    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        String version = null;
        Class serviceClass = method.getDeclaringClass();
        ApidocService apidocService = (ApidocService)serviceClass.getAnnotation(ApidocService.class);
        if(apidocService != null){
            if(apidocService.version() != null && !apidocService.version().isEmpty()){
                version = apidocService.version();
            }
        }
        boolean requestBodyRequired = true;
        String name = method.getName();
        ApidocInterface[] apidocInterfaces = method.getAnnotationsByType(ApidocInterface.class);
        if(apidocInterfaces != null){
            String version0 = null;
            String name0 = null;
            for (ApidocInterface apidocInterface : apidocInterfaces){
                if(version0 != null){
                    throw new IllegalArgumentException("存在多个!@ApidocInterface");
                }
                if(apidocInterface != null){
                    if(apidocInterface.version() != null && !apidocInterface.version().isEmpty()){
                        version0 = apidocInterface.version();
                        name0 = apidocInterface.name();
                    }
                }
            }
            if(version0 != null){
                version = version0;
            }
            if(name0 != null &&  !name0.isEmpty()){
                name = name0;
            }
        }
        Request request = null;

        InterfaceSecurityMetadata interfaceDescInfo = interfaceDescInfos.get(name + "@" + version);
        if (interfaceDescInfo == null) {
            throw new IllegalArgumentException("not exist interface name:'" + name + "' interface define info !");
        }
        if (method.getParameterTypes().length == 1) {
            if (method.getParameterTypes()[0] == Request.class) {
                request = (Request) arguments[0];
                request.setName(method.getName());
                request.setChannelNo(ctx.getChannelNo());
                request.setVersion(version);
            } else {
                if (arguments[0] == null) {
                    throw new NullPointerException();
                } else {
                    request = Request.builder().channelNo(ctx.getChannelNo()).version(version).txType(name).cipherType("AES").data(arguments[0]).build();
                }
            }
        } else if (method.getParameterTypes().length == 2) {
            int requestBodyIdx = 0;
            boolean found = false;
            boolean requestBodyRequired0 = true;
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnns : parameterAnnotations){
                if(parameterAnns != null && parameterAnns.length > 0){
                    for (Annotation annotation : parameterAnns){
                        if(annotation.annotationType() == RequestBody.class){
                            RequestBody requestBody = (RequestBody)annotation;
                            found = true;
                            requestBodyRequired0 = requestBody.required();
                            break;
                        }
                    }
                }
                if(found){
                    break;
                }
                requestBodyIdx++;
            }
            requestBodyRequired = requestBodyRequired0;
            if (method.getParameterTypes()[0] == Request.class && requestBodyIdx == 0) {
                request = (Request) arguments[0];
                request.setName(method.getName());
                request.setChannelNo(ctx.getChannelNo());
                request.setVersion(version);
            } else if (method.getParameterTypes()[1] == Request.class && requestBodyIdx == 1) {
                request = (Request) arguments[1];
                request.setName(method.getName());
                request.setChannelNo(ctx.getChannelNo());
                request.setVersion(version);
            } else if (method.getParameterTypes()[0] != Request.class && requestBodyIdx == 0) {
                if (arguments[0] == null) {
                    throw new NullPointerException();
                } else {
                    request = Request.builder().channelNo(ctx.getChannelNo()).version(version).txType(name).data(arguments[0]).build();
                }
            } else if (method.getParameterTypes()[1] != Request.class && requestBodyIdx == 1) {
                if (arguments[1] == null) {
                    throw new NullPointerException();
                } else {
                    request = Request.builder().channelNo(ctx.getChannelNo()).version(version).txType(name).data(arguments[1]).build();
                }
            } else {
                throw new RuntimeException("未知参数" + method.getParameterTypes());
            }
        }
        String sign = null;
        String cipherText = null;
        if(requestBodyRequired){
            if(request.getData() == null){
                throw new IllegalArgumentException("request body is null");
            }
        }
        if (interfaceDescInfo.isSignFirstEncryptSecond()) {
            if (interfaceDescInfo.getSignAlgorithm() != null && !interfaceDescInfo.getSignAlgorithm().isEmpty()) {
                interfaceSecurityService.sign(request, ctx.getSignPassword(), interfaceDescInfo);
            }
            if (interfaceDescInfo.getEncryptAlgorithm() != null && !interfaceDescInfo.getEncryptAlgorithm().isEmpty()) {
                interfaceSecurityService.encrypt(request, ctx.getCipherPassword(), interfaceDescInfo);
            }
        } else {
            if (interfaceDescInfo.getEncryptAlgorithm() != null && !interfaceDescInfo.getEncryptAlgorithm().isEmpty()) {
                interfaceSecurityService.encrypt(request, ctx.getCipherPassword(), interfaceDescInfo);
            }
            if (interfaceDescInfo.getSignAlgorithm() != null && !interfaceDescInfo.getSignAlgorithm().isEmpty()) {
                interfaceSecurityService.sign(request, ctx.getSignPassword(), interfaceDescInfo);
            }
        }
        Client client = new Client(ctx.getHost(), ctx.getPort(), ctx.getAppContext());
        Response ret = null;
        try {
            ret = client.call(request, ctx.getResultCallbacks().toArray(new ResultCallback[ctx.getResultCallbacks().size()]));
        } catch (Exception e) {
            throw e;
        }
        if (ret.getSignType() != null && !ret.getSignType().equals(interfaceDescInfo.getVerifyAlgorithm())) {
            throw new RuntimeException("verify algorithm is diffencent from response!");
        }
        if (ret.getCipherType() != null && !ret.getCipherType().equals(interfaceDescInfo.getDecryptAlgorithm())) {
            throw new RuntimeException("decrypt algorithm is diffencent from response!");
        }
        if (interfaceDescInfo.isVerifyFirstDecryptSecond()) {
            if (interfaceDescInfo.getVerifyAlgorithm() != null && !interfaceDescInfo.getVerifyAlgorithm().isEmpty()) {
                if (!interfaceSecurityService.verify(ret, ctx.getSignPassword(), interfaceDescInfo)) {
                    throw new RuntimeException("验签失败");
                }
            }
            if (interfaceDescInfo.getDecryptAlgorithm() != null && !interfaceDescInfo.getDecryptAlgorithm().isEmpty()) {
                if (!interfaceSecurityService.decrypt(ret, ctx.getCipherPassword(), interfaceDescInfo)){
                    throw new RuntimeException("解密失败");
                }
            }
        } else {
            if (interfaceDescInfo.getDecryptAlgorithm() != null && !interfaceDescInfo.getDecryptAlgorithm().isEmpty()) {
                if (!interfaceSecurityService.decrypt(ret, ctx.getCipherPassword(), interfaceDescInfo)){
                    throw new RuntimeException("解密失败");
                }
            }
            if (interfaceDescInfo.getVerifyAlgorithm() != null && !interfaceDescInfo.getVerifyAlgorithm().isEmpty()) {
                if (!interfaceSecurityService.verify(ret, ctx.getSignPassword(), interfaceDescInfo)) {
                    throw new RuntimeException("验签失败");
                }
            }
        }
        if (method.getReturnType() == Response.class) {
            return ret;
        } else {
            Object rsp = GSON.fromJson(ret.getData(), method.getReturnType());
            return rsp;
        }

    }
}