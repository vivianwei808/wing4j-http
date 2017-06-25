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
package org.wing4j.http.protocol.domains;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.http.protocol.code.RspCode;
import org.wing4j.interfaces.EnumStringCode;

import java.io.Serializable;

/**
 * Created by wing4j on 2017/6/25.
 * 请求对象
 */
@ToString
@EqualsAndHashCode
@Builder
public class Response<T> implements Serializable {
    private static Gson GSON = new GsonBuilder().serializeNulls().setDateFormat("yyyyMMddHHmmssSSS").create();
    /**
     * 服务名称
     */
    @Getter
    @Setter
    String service;
    /**
     * 接口名称
     */
    @Getter
    @Setter
    String name;
    /**
     * 接口版本号
     */
    @Getter
    @Setter
    String version;
    /**
     * 错误码
     */
    @Getter
    @Setter
    String code;
    /**
     * 错误信息
     */
    @Getter
    @Setter
    String desc;
    /**
     * 应答包装的类名
     */
    @Getter
    String className;
    /**
     * 签名
     */
    @Getter
    @Setter
    String sign;
    /**
     * 签名方式
     */
    @Getter
    @Setter
    String signType;
    /**
     * 加密方式
     */
    @Getter
    @Setter
    String cipherType;
    /**
     * 加密数据
     */
    @Getter
    @Setter
    String data;
    private Response(){

    }
    private Response(String service, String name, String version, String code, String desc, String sign, String signType, String cipherType, String className, String data) {
        this.service = service;
        this.name = name;
        this.version = version;
        this.code = code;
        this.desc = desc;
        this.sign = sign;
        this.signType = signType;
        this.cipherType = cipherType;
        this.className = className;
        this.data = data;
    }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        /**
         * 服务名称
         */
        @Getter
        @Setter
        String service;
        /**
         * 接口名称
         */
        @Getter
        @Setter
        String name;
        /**
         * 接口版本号
         */
        @Getter
        @Setter
        String version = "10";
        /**
         * 错误码
         */
        String code = RspCode.SUCCESS.getCode();
        /**
         * 错误信息
         */
        String desc = RspCode.SUCCESS.getDesc();
        /**
         * 签名
         */
        String sign;
        /**
         * 签名方式
         */
        String signType;
        /**
         * 加密方式
         */
        String cipherType;
        /**
         * 应答包装的类名
         */
        String className = String.class.getName();;
        /**
         * 加密数据
         */
        String data = "{}";
        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }
        public Builder code(String code){
            this.code = code;
            return this;
        }

        public Builder desc(String desc){
            this.desc = desc;
            return this;
        }

        public Builder code(EnumStringCode enumStringCode){
            if(enumStringCode != null){
                this.code = enumStringCode.getCode();
                this.desc = enumStringCode.getDesc();
            }
            return this;
        }
        public Builder sign(String sign){
            this.sign = sign;
            return this;
        }

        public Builder signType(String signType){
            this.signType = signType;
            return this;
        }

        public Builder cipherType(String cipherType){
            this.cipherType = cipherType;
            return this;
        }

        public Builder data(Object data){
            if(data != null){
                if(data instanceof String){
                    this.data = (String)data;
                    this.className = String.class.getName();
                }else{
                    String json = GSON.toJson(data);
                    this.data = json;
                    this.className = data.getClass().getName();
                }
            }else{
                this.data = "{}";
                this.className = String.class.getName();
            }
            return this;
        }

        public <T> Response<T> build(){
            if(this.code == null){
                throw new LogtrackRuntimeException(ErrorContextFactory.instance().activity("设置接口应答信息").message("code is null"));
            }
            if(this.desc == null){
                throw new LogtrackRuntimeException(ErrorContextFactory.instance().activity("设置接口应答信息").message("desc is null"));
            }
            if(this.className == null){
                throw new LogtrackRuntimeException(ErrorContextFactory.instance().activity("设置接口应答信息").message("className is null"));
            }
            Response<T> response = new Response<>(this.service, this.name, this.version, this.code, this.desc, this.sign, this.signType, this.cipherType, this.className, this.data);
            return response;
        }
    }
}
