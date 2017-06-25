package org.wing4j.http.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by wing4j on 2017/6/25.
 * 该注解与Spring Component等同，用于定义HTTP接口服务
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HttpService {
    String value() default "";

    /**
     * 用于定义该Bean所属Service服务
     * @return
     */
    String service();
}
