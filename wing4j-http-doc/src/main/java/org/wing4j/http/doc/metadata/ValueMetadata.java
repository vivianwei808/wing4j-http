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
package org.wing4j.http.doc.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/6/27.
 */
@Data
@ToString
@AllArgsConstructor
public class ValueMetadata implements Metadata{
    /**
     * 字段名称
     */
    String name;
    /**
     * 要素类型
     */
    final String metadataType = "value";
    /**
     * 字段描述
     */
    String desc;
    /**
     * 字典描述
     */
    final List<String> enumDesc = new ArrayList<>();
    /**
     * 数据类型
     */
    String dataType;
    /**
     * 是否必输
     */
    boolean required;
    /**
     * 最小长度
     */
    int minLen;
    /**
     * 最大长度
     */
    int maxLen;
    /**
     * 缺省值
     */
    String defVal;

}
