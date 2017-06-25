package org.wing4j.http.server;

/**
 * Created by wing4j on 2017/6/25.
 * 接口访问引擎
 */
public interface InterfaceAccessEngine {
    /**
     * 执行输入的JSON
     * @param reqJson 请求 JSON
     * @return 应答JSON
     */
    String execute(String reqJson);
}
