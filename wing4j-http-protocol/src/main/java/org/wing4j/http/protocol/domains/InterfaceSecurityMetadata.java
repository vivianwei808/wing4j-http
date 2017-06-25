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

import lombok.Data;
import lombok.ToString;
import org.wing4j.doc.api.annotation.ApidocElement;

/**
 * Created by wing4j on 2017/6/25.
 * 接口描述信息
 */
@Data
@ToString
public class InterfaceSecurityMetadata {
    @ApidocElement("接口名称")
    String name;
    @ApidocElement("接口版本")
    String version;
    @ApidocElement("接口描述")
    String desc;
    @ApidocElement("加密算法")
    String encryptAlgorithm;
    @ApidocElement("解密算法")
    String decryptAlgorithm;
    @ApidocElement("签字算法")
    String signAlgorithm;
    @ApidocElement("验签算法")
    String verifyAlgorithm;
    @ApidocElement("签字优先于加密")
    boolean signFirstEncryptSecond;
    @ApidocElement("验证签字优先于解密")
    boolean verifyFirstDecryptSecond;

}
