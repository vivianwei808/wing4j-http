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
package org.wing4j.http.doc;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.wing4j.common.utils.GenericUtil;
import org.wing4j.doc.api.annotation.ApidocInterface;
import org.wing4j.doc.api.annotation.ApidocService;
import org.wing4j.http.doc.metadata.InterfaceMetadata;
import org.wing4j.http.doc.metadata.Metadata;
import org.wing4j.http.doc.metadata.ServiceMetadata;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wing4j on 2017/6/27.
 */
@Slf4j
public class DocScanner {
    final Map<ServiceMetadata, List<InterfaceMetadata>> INTERFACE_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据系统标识列出接口列表
     */
    public synchronized DocScanner init(boolean ifErrorThrow, String docScanPackage) {
        //初始化扫描器
        INTERFACE_CACHE.clear();
        //使用类扫描，扫描符合条件的类
        Reflections reflections = new Reflections(docScanPackage);
        Set<Class<?>> apiDocServices = reflections.getTypesAnnotatedWith(ApidocService.class);
        log.debug("scan @ApidocService size {}", apiDocServices.size());
        log.debug("scan @ApidocService {}", apiDocServices);
        for (Class<?> apiDocService : apiDocServices) {
            log.debug("begin extract {}", apiDocService);
            if (!apiDocService.isInterface()) {
                log.debug("{} is not interface", apiDocService);
                continue;
            }
            ApidocService apidocService = apiDocService.getAnnotation(ApidocService.class);
            String usage0 = apidocService.usage();
            if (apidocService.usage() == null || apidocService.usage().isEmpty()) {
                usage0 = "";
            }
            ServiceMetadata serviceMetadata = new ServiceMetadata(apidocService.value(), apiDocService.getName(), usage0);
            List<InterfaceMetadata> interfaceInfos = new ArrayList<>();
            INTERFACE_CACHE.put(serviceMetadata, interfaceInfos);
            Method[] methods = apiDocService.getMethods();
            //对每一个方法进行遍历，如果没有Interfa注解则直接跳过
            for (Method m : methods) {
                //只要接口中一个类有Interface注解，则将该类返回
                ApidocInterface apidocInterface = m.getAnnotation(ApidocInterface.class);
                if (apidocInterface == null) {
                    log.debug("{}.{} 未标注@ApidocInterface", apiDocService.getName(), m.getName());
                    break;
                }

                String desc = apidocInterface.value();
                String name = apiDocService.getName() + "." + m.getName();
                String txType = m.getName();
                String usage = apidocInterface.usage();
                if (apidocInterface.name() != null && !apidocInterface.name().isEmpty()) {
                    txType = apidocInterface.name();
                }
                if (apidocInterface.usage() == null || apidocInterface.usage().isEmpty()) {
                    usage = usage0;
                }
                InterfaceMetadata interfaceMetadata = new InterfaceMetadata(name, txType, desc, usage, apiDocService.getName(), m.getName());
                interfaceInfos.add(interfaceMetadata);
                Class requestClazz = null;
                int idx = 0;
                if (m.getParameterTypes().length > 0) {
                    //获取方法的请求参数和应答参数
                    if (m.getParameterTypes().length == 1) {
                        requestClazz = m.getParameterTypes()[0];
                        idx = 0;
                    } else if (m.getParameterTypes().length == 2) {
                        boolean ok = false;
                        for (Class paramType : m.getParameterTypes()) {
                            if (paramType.getName().equals("com.zbj.finance.platform.ChannelContext")) {
                                //跳过
                            } else {
                                if (ok) {
                                    //TODO 存在多个Request类型入参
                                    break;
                                }
                                ok = true;
                                requestClazz = paramType;
                                break;
                            }
                            idx++;
                        }
                        if (!ok) {
                            break;
                        }
                    } else {
                        //TODO 存在过多参数
                        break;
                    }
                }else{
                    //无参数
                    break;
                }
                try {
                    if (requestClazz.getName().equals("com.zbj.finance.platform.domain.Request")) {
                        Class[] classes = GenericUtil.extractInterfaceMethodParams(apiDocService, m.getName(), m.getParameterTypes(), idx);
                        if (classes.length > 1) {
                            //TODO 个入参泛型信息不为一个
                            break;
                        }else if(classes.length == 0){
                            //TODO 个入参泛型信息未添加
                            break;
                        }
                        requestClazz = classes[0];
                    }
                } catch (Exception e) {
                    if (ifErrorThrow) {
                        throw e;
                    }
                }
                Class returnClazz = m.getReturnType();

                if (returnClazz.getName().equals("com.zbj.finance.platform.domain.Response")) {
                    Class[] classes = null;
                    try {
                        classes = GenericUtil.extractInterfaceMethodReturn(apiDocService, m.getName(), m.getParameterTypes());
                        if (classes.length != 1) {
                            //返回值泛型信息不为一个
                            break;
                        }
                        returnClazz = classes[0];
                    } catch (Exception e) {
                        if (ifErrorThrow) {
                            throw e;
                        }
                    }
                }
                BeanExtractor extractor = new BeanExtractor();
                List<Metadata> requests = extractor.extract(requestClazz);
                List<Metadata> responses = extractor.extract(returnClazz);
                interfaceMetadata.getRequest().addAll(requests);
                interfaceMetadata.getResponse().addAll(responses);
            }
            log.debug("end extract {}", apiDocService);
        }
        return this;
    }

    public List<ServiceMetadata> listService() {
        List<ServiceMetadata> list = new ArrayList<>(INTERFACE_CACHE.keySet());
        Collections.sort(list);
        return list;
    }

    public List<InterfaceMetadata> listInterface(String className) {
        for (ServiceMetadata serviceMetadata : INTERFACE_CACHE.keySet()) {
            if (serviceMetadata.getClassName().equals(className)) {
                List<InterfaceMetadata> list = INTERFACE_CACHE.get(serviceMetadata);
                Collections.sort(list);
                return list;
            }
        }
        return null;
    }

    public InterfaceMetadata listInterface(String className, String methodName) {
        for (ServiceMetadata serviceMetadata : INTERFACE_CACHE.keySet()) {
            if (serviceMetadata.getClassName().equals(className)) {
                List<InterfaceMetadata> interfaceInfoList = INTERFACE_CACHE.get(serviceMetadata);
                for (InterfaceMetadata interfaceMetadata : interfaceInfoList) {
                    if (interfaceMetadata.getMethodName().equals(methodName)) {
                        return interfaceMetadata;
                    }
                }
            }
        }
        return null;
    }

}
