package org.wing4j.http.server.metadata;

import lombok.Data;
import lombok.ToString;

/**
 * Created by wing4j on 2017/6/25.
 */
@Data
@ToString
public class InterfaceServiceDefineMetadata {
    /**
     * 服务名称
     */
    String service;
    /**
     * 签字密码
     */
    String signPassword;

    /**
     * 加密密码
     */
    String cipherPassword;
}
