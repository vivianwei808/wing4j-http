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

import org.wing4j.http.protocol.code.RspCode;
import org.wing4j.http.protocol.domains.FetchInterfaceRequest;
import org.wing4j.http.protocol.domains.FetchInterfaceResponse;
import org.wing4j.http.protocol.domains.InterfaceSecurityMetadata;
import org.wing4j.http.protocol.service.InterfaceService;
import org.wing4j.security.AesUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wing4j on 2017/6/25.
 */
public final class Endpoint {
    static Timer timer = null;
    static ConcurrentMap<String, Map<String, InterfaceSecurityMetadata>> CHANNEL_CACHE = new ConcurrentHashMap<>();
    static ResultCallback FAIL_RESULT_CALLBACK = new ResultCallback() {
        @Override
        public <Req, Rsp> boolean  accept(String rspCode, String rspDesc, Req request, Rsp response){
            return RspCode.valueOfCode(rspCode) != RspCode.SUCCESS;
        }

        @Override
        public <Req, Rsp> boolean  handle(Req request, Rsp response, String body) {
            throw new RuntimeException(body);
        }
    };

    private Endpoint() {
    }

    static void init(final String host, final int port, final String appContext, final String service, final String signPassword, final String cipherPassword) {
        if (timer == null) {
            synchronized (Endpoint.class) {
                if (timer == null) {
                    timer = new Timer("fetch-tx-timer", true);
                }
            }
        }
        ServiceContext ctx = ServiceContext.builder().host(host).port(port).appContext(appContext).signPassword(signPassword).cipherPassword(cipherPassword).build();
        InterfaceSecurityMetadata interfaceDescInfo = new InterfaceSecurityMetadata();
        interfaceDescInfo.setName("fetchInterfaceDefine");
        interfaceDescInfo.setVersion("1.0.0");
        interfaceDescInfo.setDesc("拉取接口描述信息");
        interfaceDescInfo.setSignAlgorithm("MD5");
        interfaceDescInfo.setVerifyAlgorithm("MD5");
        interfaceDescInfo.setEncryptAlgorithm("AES");
        interfaceDescInfo.setDecryptAlgorithm("AES");
        Map<String, InterfaceSecurityMetadata> interfaceDescInfoMap = new HashMap<>();
        interfaceDescInfoMap.put(interfaceDescInfo.getName() + "@" + interfaceDescInfo.getVersion(), interfaceDescInfo);
        final InterfaceService interfaceService = wrap(InterfaceService.class, ctx, interfaceDescInfoMap);
        final FetchInterfaceRequest request = FetchInterfaceRequest.builder().service("interfaceService").build();
        try {
            fetch(cipherPassword, request, interfaceService, service);
        } catch (Throwable e) {
            throw new RuntimeException("fetch interface define information happens error!");
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    fetch(cipherPassword, request, interfaceService, service);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }, 60 * 1000, 60 * 1000);
    }

    private static void fetch(String cipherPassword, FetchInterfaceRequest request, InterfaceService interfaceService, String service) throws UnsupportedEncodingException {
        String token = AesUtils.encrypt(String.valueOf(System.currentTimeMillis()), cipherPassword, "UTF-8");
        request.setToken(token);
        FetchInterfaceResponse response = interfaceService.fetchInterfaceDefine(request);
        Map<String, InterfaceSecurityMetadata> interfaces = new HashMap();
        for (InterfaceSecurityMetadata interfaceDescInfo : response.getInterfaces()) {
            interfaces.put(interfaceDescInfo.getName() + "@" + interfaceDescInfo.getVersion(), interfaceDescInfo);
        }
        CHANNEL_CACHE.put(service, interfaces);
    }

    public static <T> T lookup(Class<T> serviceClass, ServiceContext ctx) {
        if (serviceClass == null) {
            throw new NullPointerException("call service class is null");
        }
        if (!serviceClass.isInterface()) {
            throw new IllegalArgumentException("call service '" + serviceClass + "'class is not interface!");
        }
        ctx.getResultCallbacks().add(FAIL_RESULT_CALLBACK);
        if (!CHANNEL_CACHE.containsKey(ctx.getChannelNo())) {
            synchronized (Endpoint.class) {
                if (!CHANNEL_CACHE.containsKey(ctx.getChannelNo())) {
                    init(ctx.getHost(), ctx.getPort(), ctx.getAppContext(), ctx.getChannelNo(), ctx.getSignPassword(), ctx.getCipherPassword());
                }
            }
        }
        return wrap(serviceClass, ctx, CHANNEL_CACHE.get(ctx.getChannelNo()));
    }

    /**
     * 将接口包装为服务对象
     *
     * @param serviceClass
     * @param ctx
     * @param interfaceDescInfos
     * @param <T>
     * @return
     * @throws Exception
     */
    static <T> T wrap(final Class<T> serviceClass, final ServiceContext ctx, final Map<String, InterfaceSecurityMetadata> interfaceDescInfos) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new Invoker(ctx, interfaceDescInfos));
    }
}