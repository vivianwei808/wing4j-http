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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;

import java.io.Serializable;

/**
 * Created by wing4j on 2017/6/25.
 * 请求对象
 */
@ToString
@EqualsAndHashCode
public class Request<T> implements Serializable {
    private static Gson GSON = new GsonBuilder().serializeNulls().setDateFormat("yyyyMMddHHmmssSSS").create();
    /**
     * 通道编号
     */
    @Getter
    @Setter
    String channelNo;
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
    String signType = "MD5";
    /**
     * 加密方式
     */
    @Getter
    @Setter
    String cipherType = "AES";
    /**
     * 请求的类对象
     */
    @Getter
    String className;
    /**
     * 加密数据
     */
    @Getter
    @Setter
    String data;

    private Request() {

    }

    private Request(String channelNo, String name, String version, String sign, String signType, String cipherType, String className, String data) {
        this.channelNo = channelNo;
        this.name = name;
        this.version = version;
        this.sign = sign;
        this.signType = signType;
        this.cipherType = cipherType;
        this.className = className;
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 服务名称
         */
        String channelNo;
        /**
         * 交易码
         */
        String txType;
        /**
         * 接口版本号
         */
        String version;
        /**
         * 签名
         */
        String sign;
        /**
         * 签名方式
         */
        String signType = "MD5";
        /**
         * 加密方式
         */
        String cipherType = "AES";
        /**
         * 请求的类对象
         */
        String className;
        /**
         * 加密数据
         */
        String data;

        public Builder channelNo(String channelNo) {
            this.channelNo = channelNo;
            return this;
        }

        public Builder txType(String txType) {
            this.txType = txType;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder signType(String signType) {
            this.signType = signType;
            return this;
        }

        public Builder cipherType(String cipherType) {
            this.cipherType = cipherType;
            return this;
        }

        public Builder data(Object data) {
            if (data != null) {
                if (data instanceof String) {
                    this.data = (String) data;
                    this.className = String.class.getName();
                } else {
                    String json = GSON.toJson(data);
                    this.data = json;
                    this.className = data.getClass().getName();
                }
            }
            return this;
        }

        public <T> Request<T> build() {
            if (this.data == null) {
               throw new LogtrackRuntimeException(ErrorContextFactory.instance().activity("设置接口应答信息").message("data is null"));
            }
            if (this.className == null) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance().activity("设置接口应答信息").message("className is null"));
            }
            Request<T> request = new Request<>(this.channelNo, this.txType, this.version, this.sign, this.signType, this.cipherType, this.className, this.data);
            return request;
        }
    }
}
