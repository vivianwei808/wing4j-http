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
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2016/7/15.
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class InterfaceMetadata implements Comparable<InterfaceMetadata>{
    /**
     * 接口名称
     */
    String name;
    /**
     * 交易码
     */
    String txType;
    /**
     * 接口描述
     */
    String desc;
    /**
     * 用法
     */
    String usage;
    /**
     * 接口类
     */
    String className;
    /**
     * 方法对象
     */
    String methodName;
    /**
     * 请求
     */
    final List<Metadata> request = new ArrayList<>();
    /**
     * 应答
     */
    final List<Metadata> response = new ArrayList<>();

    @Override
    public int compareTo(InterfaceMetadata o) {
        if(name == null && o != null && o.name != null){
            return 1;
        }
        if(name != null && o == null){
            return -1;
        }
        return name.compareTo(o.name);
    }
}