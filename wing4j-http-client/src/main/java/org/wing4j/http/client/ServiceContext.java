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

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 * 服务调用上下文
 */
@Data
@Builder
public final class ServiceContext {
    /**
     * 结果回调处理
     */
    final List<ResultCallback> resultCallbacks = new ArrayList<>();
    /**
     * 主机地址
     */
    String host;
    /**
     * 端口号
     */
    int port;
    /**
     * 应用服务器上下文名称
     */
    String appContext = "";
    /**
     * 通道编号
     */
    String channelNo;
    /**
     * 签字密码
     */
    String signPassword;

    /**
     * 加密密码
     */
    String cipherPassword;
}
