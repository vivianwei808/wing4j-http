package org.wing4j.http.server.metadata;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Method;

/**
 * Created by wing4j on 2017/6/25.
 * 接口定义元信息
 */
@Data
@ToString
@Builder
public class InterfaceDefineMetadata {
    /**
     * 服务名称
     */
    String serviceName;
    /**
     * 服务类对象
     */
    Class serviceClass;
    /**
     * 接口名称
     */
    String interfaceName;
    /**
     * 接口版本号
     */
    String version;
    /**
     * 接口描述
     */
    String desc;
    /**
     * 接口用法
     */
    String usage;
    /**
     * 方法名称
     */
    Method method;
    /**
     * 参数类型
     */
    Class[] paramTypes;

}
