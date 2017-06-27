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

import org.wing4j.doc.api.annotation.ApidocElement;
import org.wing4j.http.doc.metadata.FormMetadata;
import org.wing4j.http.doc.metadata.Metadata;
import org.wing4j.http.doc.metadata.ValueMetadata;
import org.wing4j.interfaces.EnumIntegerCode;
import org.wing4j.interfaces.EnumStringCode;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/6/27.
 */
public class BeanExtractor {
    public List<Metadata> extract(Class clazz) {
        return extract0(clazz, 0);
    }

    /**
     * @param clazz
     * @return
     */
    List<Metadata> extract0(Class clazz, int deep) {
        deep++;
        if (deep > 10) {
            throw new IllegalArgumentException("深度过深，存在循环");
        }
        List<Metadata> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ApidocElement element = field.getAnnotation(ApidocElement.class);
            if (element == null) {
                continue;
            }
            String desc = element.value();
            Metadata metadata = null;
            List<String> enums = new ArrayList<>();
            if(element.enumClass() != Enum.class){
                if(EnumStringCode.class.isAssignableFrom(element.enumClass())
                        || EnumIntegerCode.class.isAssignableFrom(element.enumClass())){
                    enums =  extractEnum(element.enumClass());
                }else {
                    //TODO 设置的字典枚举无效
                }
            }
            if (!List.class.isAssignableFrom(field.getType())) {
                ValueMetadata valueMetadata = new ValueMetadata(field.getName(), desc, field.getType().getSimpleName(), element.required(), element.minLen(), element.maxLen(), element.defaultValue());
                valueMetadata.getEnumDesc().addAll(enums);
                metadata = valueMetadata;
            } else {
                FormMetadata formMetadata = new FormMetadata(field.getName(), desc);
                metadata = formMetadata;
                Class clazz0 = field.getType();
                if( clazz0 == String.class || clazz0 == Integer.class){
                    //TODO
                }else {
                    Type type = field.getGenericType();
                    //检查泛型
                    if (type instanceof ParameterizedType) {
                        //获取泛型
                        ParameterizedType target = (ParameterizedType) type;
                        Type[] parameters = target.getActualTypeArguments();
                        Class<?> modelClass = (Class<?>) parameters[0];
                        //对多条的数据进行提取
                        formMetadata.getElements().addAll(extract0(modelClass, deep));
                    }
                }

            }
            list.add(metadata);
        }
        return list;
    }

    /**
     * 根据传入的枚举类型提取描述信息
     *
     * @param enumClass
     * @return
     */
    List<String> extractEnum(Class<?> enumClass) {
        Object[] values = enumClass.getEnumConstants();
        Method getCodeMethod = null;
        Method getDescMethod = null;
        try {
            getCodeMethod = enumClass.getMethod("getCode");
            getDescMethod = enumClass.getMethod("getDesc");
        } catch (NoSuchMethodException e) {
            return new ArrayList<>();
        }
        if (getCodeMethod != null && getDescMethod != null) {
            List<String> list = new ArrayList<>();
            for (Object val : values) {
                try {
                    Object code = getCodeMethod.invoke(val);
                    Object desc = getDescMethod.invoke(val);
                    list.add(code + ":" + desc);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
        return new ArrayList<>();
    }

}
