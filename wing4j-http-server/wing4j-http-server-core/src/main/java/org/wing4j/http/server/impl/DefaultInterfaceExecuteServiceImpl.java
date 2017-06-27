package org.wing4j.http.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;
import org.wing4j.http.server.InterfaceExecuteService;

import java.lang.reflect.Method;

/**
 * Created by wing4j on 2017/6/25.
 * 默认的接口执行服务
 */
@Service
public class DefaultInterfaceExecuteServiceImpl implements InterfaceExecuteService, ApplicationContextAware {
    ApplicationContext applicationContext;
    @Override
    public Response call(Request request, InterfaceDefineMetadata interfaceDefineMetadata) {
        Method method = interfaceDefineMetadata.getMethod();
        //获取实例
        Object instance = null;
        if(StringUtils.isBlank(interfaceDefineMetadata.getServiceName())){
            instance = applicationContext.getBean(interfaceDefineMetadata.getServiceName());
        }else{
            instance = applicationContext.getBean(interfaceDefineMetadata.getServiceClass());
        }
        InterfaceInvoker invoker = new InterfaceInvoker(method);
        //执行服务
        return invoker.invoke(instance, request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
