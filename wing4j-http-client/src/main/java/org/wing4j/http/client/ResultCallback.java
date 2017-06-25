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

/**
 * 结果回调处理
 */
public interface ResultCallback {
    /**
     * 是否满足接收条件
     * @param rspCode 应答码
     * @param rspDesc 应答描述
     * @param request 应答信息
     * @param response 应答信息
     * @param <Req>
     * @param <Rsp>
     * @return
     */
    <Req, Rsp> boolean  accept(String rspCode, String rspDesc, Req request, Rsp response);

    /**
     * 处理结果
     * @param request 应答信息
     * @param response 应答信息
     * @param body 返回正文
     * @param <Req>
     * @param <Rsp>
     * @return
     */
    <Req, Rsp> boolean  handle(Req request, Rsp response, String body);
}
