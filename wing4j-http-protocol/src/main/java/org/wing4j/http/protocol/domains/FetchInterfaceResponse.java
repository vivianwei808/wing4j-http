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

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.wing4j.doc.api.annotation.ApidocElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/6/25.
 */
@Data
@ToString
@Builder
public class FetchInterfaceResponse {
    @ApidocElement("通道编号")
    String channelNo;
    @ApidocElement("接口列表")
    final List<InterfaceSecurityMetadata> interfaces = new ArrayList<>();
}
