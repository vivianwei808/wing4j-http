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
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;


/**
 * Created by wing4j on 2017/6/25.
 */
public class Client {
    static Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();
    /**
     * 主机地址
     */
    String host;
    /**
     * 端口号
     */
    int port;
    /**
     * 应用名
     */
    String appContext;

    String address;


    public Client(String host, int port, String appContext) {
        this.host = host;
        this.port = port;
        this.appContext = appContext;
        address = "http://" + host + ":" + port + appContext + "/api";
    }


    /**
     * 调用远程接口
     *
     * @param request         请求
     * @param resultCallbacks 状态回调
     * @return 应答
     */
    public Response call(Request request, ResultCallback... resultCallbacks) {
        final String reqData = GSON.toJson(request);
        Http http = null;
        try {
            http = Http.post(address)
                    .acceptCharset("UTF-8")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .useCaches(false)//不允许缓存
                    .contentType("application/json;text/plain", "UTF-8");
            http.send(reqData);
        } catch (Exception e) {
            //处理异常
            throw new RuntimeException("call address:" + address + " happens error! request data :" + reqData);
        }
        Response response = null;
        //如果返回200
        final String body = http.body("UTF-8");
        if (http.ok()) {
            try {
                response = GSON.fromJson(body, Response.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("call interface name '" + request.getName() + "' happens error! cause return response is not json!", e);
            }
            if (response == null) {
                throw new IllegalArgumentException("call interface name '" + request.getName() + "' happens error! cause return response is null");
            }
            //如果返回404
        } else if (http.notFound()) {
            throw new IllegalArgumentException("not reachable '" + address + "'");
        } else {//处理不了的未知错误
            throw new IllegalArgumentException("call  '" + address + "' happens error code :" + http.code());
        }
        for (ResultCallback resultCallback : resultCallbacks) {
            try {
                if (resultCallback.accept(response.getCode(), response.getDesc(), request, response)) {
                    resultCallback.handle(request, response, body);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return response;
    }


}
