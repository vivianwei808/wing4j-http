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
package org.wing4j.http.protocol.code;

import org.wing4j.interfaces.EnumStringCode;

/**
 * Created by wing4j on 2017/6/25.
 */
public enum  RspCode implements EnumStringCode{
    SUCCESS("200","成功"),
    INTERFACE_NOT_DEFINE("-1","接口未定义"),
    VERIFY_FAIL("-2","验签失败"),
    DECRYPT_FAIL("-3","解密失败"),
    ENCRYPT_HAPPENS_ERROR("-4","加密发生错误"),
    SIGN_HAPPENS_ERROR("-3","签字发生错误");
    String code;
    String desc;
    private RspCode(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public static RspCode valueOfCode(String code){
        RspCode[] values = values();
        for (RspCode rspCode : values){
            if(rspCode.code.equals(code)){
                return rspCode;
            }
        }
        throw new IllegalArgumentException("illegal code :" + code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
