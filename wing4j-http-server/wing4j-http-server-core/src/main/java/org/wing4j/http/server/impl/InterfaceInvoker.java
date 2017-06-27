package org.wing4j.http.server.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wing4j.common.logtrack.ErrorContext;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.http.protocol.annotation.RequestBody;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wing4j on 2017/6/25.
 * 接口执行器
 */
public final class InterfaceInvoker {
    static Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();
    Method interfaceMethod;
    Class returnClass;
    Class[] parameterClasses;

    InterfaceInvoker(Method interfaceMethod) {
        this.interfaceMethod = interfaceMethod;
        this.parameterClasses = interfaceMethod.getParameterTypes();
        this.returnClass = interfaceMethod.getReturnType();
    }

    public Response invoke(Object instance, Request request) {
        Object[] params = new Object[interfaceMethod.getParameterTypes().length];
        if (parameterClasses.length == 1) {
            if (interfaceMethod.getParameterTypes()[0] == Request.class) {
                params[0] = request;
            } else {
                params[0] = GSON.fromJson(request.getData(), parameterClasses[0]);
            }
        } else if (parameterClasses.length == 2) {
            int requestBodyIdx = 0;
            boolean found = false;
            Annotation[][] parameterAnnotations = interfaceMethod.getParameterAnnotations();
            for (Annotation[] parameterAnns : parameterAnnotations) {
                if (parameterAnns != null && parameterAnns.length > 0) {
                    for (Annotation annotation : parameterAnns) {
                        if (annotation.annotationType() == RequestBody.class) {
                            RequestBody requestBody = (RequestBody) annotation;
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    break;
                }
                requestBodyIdx++;
            }
            if (parameterClasses[0] == Request.class && requestBodyIdx == 0) {
                params[0] = request;
            } else if (parameterClasses[1] == Request.class && requestBodyIdx == 1) {
                params[1] = request;
            } else if (parameterClasses[0] != Request.class && requestBodyIdx == 0) {
                params[0] = GSON.fromJson(request.getData(), parameterClasses[0]);
            } else if (parameterClasses[1] != Request.class && requestBodyIdx == 1) {
                params[1] = GSON.fromJson(request.getData(), parameterClasses[1]);
            } else {
                throw new RuntimeException("unknown method param '" + interfaceMethod.getParameterTypes() + "'");
            }
        }
        Object response = null;
        try {
            response = interfaceMethod.invoke(instance, params);
        } catch (IllegalAccessException e) {
            ErrorContext errorContext = ErrorContextFactory.instance()
                    .activity("execute interface")
                    .message("method {}.{} is not public ", interfaceMethod.getDeclaringClass(), interfaceMethod.getName())
                    .solution("please change method '{}.{}' private to public", interfaceMethod.getDeclaringClass(), interfaceMethod.getName());
            throw new LogtrackRuntimeException(errorContext);
        } catch (InvocationTargetException e) {
            //获取真实的异常
            Throwable t = e.getTargetException();
            ErrorContext errorContext = ErrorContextFactory.instance()
                    .activity("execute interface")
                    .message("method {}.{} execute happens error! ", interfaceMethod.getDeclaringClass(), interfaceMethod.getName())
                    .solution("请根据cause进行处理服务'{}.{}'里的逻辑", interfaceMethod.getDeclaringClass(), interfaceMethod.getName() ).cause(t);
            throw new LogtrackRuntimeException(errorContext);
        }catch (Throwable e){
            ErrorContext errorContext = ErrorContextFactory.instance()
                    .activity("execute interface")
                    .message("method {}.{} execute happens error! ", interfaceMethod.getDeclaringClass(), interfaceMethod.getName())
                    .solution("unknown error!").cause(e);
            throw new LogtrackRuntimeException(errorContext);
        }
        if (returnClass == Response.class) {
            return (Response)response;
        }else{
            Response response1 = Response.builder().channelNo(request.getChannelNo()).name(request.getName()).version(request.getVersion()).data(response).build();
            return response1;
        }
    }
}
