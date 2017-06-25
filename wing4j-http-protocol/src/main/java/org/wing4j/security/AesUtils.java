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
package org.wing4j.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
/**
 * Created by wing4j on 2017/6/25.
 */


@Slf4j
public class AesUtils {
    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密的字节数组
     */
    public static byte[] encrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //需要对密码进行SHA填充
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes("UTF-8"));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            log.error("使用AES加密发生错误", e);
            return null;
        }
    }

    /**
     * 加密
     * @param plaintext 需要加密的内容
     * @param password 密码
     * @param encoding 编码集
     * @return 加密的字符串
     * @throws UnsupportedEncodingException 无法转换的字符集
     */
    public static String encrypt(String plaintext, String password, String encoding) throws UnsupportedEncodingException {

        byte[] input = Base64.encodeBase64(plaintext.getBytes(encoding));
        byte[] results = encrypt(input, password);
        byte[] base64bytes = Base64.encodeBase64(results);
        String ciphertext = new String(base64bytes);
        log.debug("plaintext:{} ciphertext:{}", plaintext, ciphertext);
        return ciphertext;
    }
    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return 解密的字节数组
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //需要对密码进行SHA填充
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes("UTF-8"));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 解密
        } catch (Exception e) {
            log.error("使用AES解密发生错误", e);
            return null;
        }
    }

    /**
     *
     * @param ciphertext 待解密内容
     * @param password 解密密钥
     * @param encoding 编码集
     * @return 加密的字符串
     * @throws UnsupportedEncodingException 无法转换的字符集
     */
    public static String decrypt(String ciphertext, String password, String encoding) throws UnsupportedEncodingException {
        byte[] input = Base64.decodeBase64(ciphertext.getBytes(encoding));
        byte[] results = decrypt(input, password);
        byte[] base64bytes = Base64.decodeBase64(results);
        String plaintext = new String(base64bytes);
        log.debug("ciphertext:{} plaintext:{}", ciphertext, plaintext);
        return plaintext;
    }

}
