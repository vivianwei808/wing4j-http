package org.wing4j.http.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.wing4j.doc.api.annotation.ApidocInterface;
import org.wing4j.doc.api.annotation.ApidocService;
import org.wing4j.http.annotation.HttpService;
import org.wing4j.http.server.InterfaceLookupService;
import org.wing4j.http.server.metadata.InterfaceDefineMetadata;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wing4j on 2017/6/25.
 * 默认的接口发现服务
 */
@Service
public class DefaultInterfaceLookupServiceImpl implements InterfaceLookupService, ApplicationContextAware {
    /**
     * 键存放交易码，值存放接口元信息
     */
    static final ConcurrentMap<String, InterfaceDefineMetadata> CACHE_SERVICE = new ConcurrentHashMap();
    @Override
    public InterfaceDefineMetadata lookup(String interfaceName, String version) {
        return CACHE_SERVICE.get(interfaceName + "@" + version);
    }

    @Override
    public void scan() {
        Map<String, Object> httpServices = this.applicationContext.getBeansWithAnnotation(HttpService.class);
        for (Object instance : httpServices.values()) {
            Class httpServiceClass = instance.getClass();
            HttpService httpService = (HttpService)httpServiceClass.getAnnotation(HttpService.class);
            ApidocService apidocService = (ApidocService)httpServiceClass.getAnnotation(ApidocService.class);
            if(apidocService == null){
                //TODO
                throw new IllegalArgumentException(httpServiceClass.toString());
            }
            for (Method method : httpServiceClass.getMethods()){
                ApidocInterface apidocInterface = method.getAnnotation(ApidocInterface.class);
                if(apidocInterface == null){
                    continue;
                }
                String name = method.getName();
                if(StringUtils.isNotBlank(apidocInterface.name())){
                    name = apidocInterface.name();
                }
                String version = apidocService.version();
                if(StringUtils.isNotBlank(apidocInterface.version())){
                    version = apidocInterface.version();
                }
                String usage = apidocInterface.usage();
                String desc = apidocInterface.value();
                InterfaceDefineMetadata interfaceDefineMetadata = InterfaceDefineMetadata.builder()
                        .serviceName(httpService.value())
                        .serviceClass(httpServiceClass)
                        .interfaceName(name)
                        .version(version)
                        .usage(usage)
                        .desc(desc)
                        .build();
                CACHE_SERVICE.put(interfaceDefineMetadata.getInterfaceName() + "@" + interfaceDefineMetadata.getVersion(), interfaceDefineMetadata);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    ApplicationContext applicationContext;
}
