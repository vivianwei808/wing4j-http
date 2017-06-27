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
package org.wing4j.common.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wing4j on 2017/6/26.
 */
public class GenericUtil {
    /**
     * 提取接口上的泛型信息
     *
     * @param interfaceClass 接口类
     * @param targetClass    目标接口
     * @return 泛型数组
     */
    public static Class[] extractInterface(Class interfaceClass, Class targetClass) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("interfaceClass is null");
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("targetClass is null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass is not interface!");
        }
        if (!targetClass.isInterface()) {
            throw new IllegalArgumentException("targetClass is not interface!");
        }
        //获取接口
        Type[] types = interfaceClass.getGenericInterfaces();
        for (Type type : types) {
            //检查泛型
            if (type instanceof ParameterizedType) {
                //获取泛型
                Class rawType = (Class) ((ParameterizedType) type).getRawType();
                if (targetClass.isAssignableFrom(rawType)) {
                    ParameterizedType target = (ParameterizedType) type;
                    Type[] parameters = target.getActualTypeArguments();
                    Class[] classes = new Class[parameters.length];
                    int idx = 0;
                    for (Type type0 : parameters) {
                        classes[idx] = (Class) type0;
                        idx++;
                    }
                    return classes;
                }
            }
        }
        return new Class[0];
    }
    /**
     * 提取接口方法参数上的泛型信息,提取第一个参数
     *
     * @param interfaceClass 接口类
     * @param methodName     方法名
     * @param methodParams   方法参数
     * @return 泛型数组
     */
    public static Class[] extractInterfaceMethodParams(Class interfaceClass, String methodName, Class[] methodParams){
        return extractInterfaceMethodParams(interfaceClass, methodName, methodParams, 0);
    }
    /**
     * 提取接口方法参数上的泛型信息
     *
     * @param interfaceClass 接口类
     * @param methodName     方法名
     * @param methodParams   方法参数
     * @param paramIndex     参数索引
     * @return 泛型数组
     */
    public static Class[] extractInterfaceMethodParams(Class interfaceClass, String methodName, Class[] methodParams, int paramIndex){
        if (interfaceClass == null) {
            throw new IllegalArgumentException("interfaceClass is null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass is not interface!");
        }
        Method method = null;
        try {
            method = interfaceClass.getMethod(methodName, methodParams);
        } catch (NoSuchMethodException e) {
           throw new IllegalArgumentException(e);
        }
        Type[] types = method.getGenericParameterTypes();
        if (paramIndex < 0) {
            throw new IllegalArgumentException("paramIndex 从0开始!");
        }
        if (paramIndex >= types.length) {
            throw new IllegalArgumentException("paramIndex 超过了参数个数!");
        }
        Type type = types[paramIndex];
        //检查泛型
        if (type instanceof ParameterizedType) {
            //获取泛型
            Class rawType = (Class) ((ParameterizedType) type).getRawType();
            ParameterizedType target = (ParameterizedType) type;
            Type[] parameters = target.getActualTypeArguments();
            Class[] classes = new Class[parameters.length];
            int idx = 0;
            for (Type type0 : parameters) {
                classes[idx] = (Class) type0;
                idx++;
            }
            return classes;
        }
        return new Class[0];
    }

    /**
     * 提取接口方法返回上的泛型信息
     *
     * @param interfaceClass 接口类
     * @param methodName     方法名
     * @param methodParams   方法参数
     * @return 泛型数组
     */
    public static Class[] extractInterfaceMethodReturn(Class interfaceClass, String methodName, Class[] methodParams){
        if (interfaceClass == null) {
            throw new IllegalArgumentException("interfaceClass is null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass is not interface!");
        }
        Method method = null;
        try {
            method = interfaceClass.getMethod(methodName, methodParams);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        Type type = method.getGenericReturnType();
        //检查泛型
        if (type instanceof ParameterizedType) {
            //获取泛型
            ParameterizedType target = (ParameterizedType) type;
            Type[] parameters = target.getActualTypeArguments();
            Class[] classes = new Class[parameters.length];
            int idx = 0;
            for (Type type0 : parameters) {
                classes[idx] = (Class) type0;
                idx++;
            }
            return classes;
        }
        return new Class[0];
    }
}
