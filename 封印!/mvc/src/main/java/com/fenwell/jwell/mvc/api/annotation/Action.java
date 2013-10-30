package com.fenwell.jwell.mvc.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fenwell.jwell.mvc.api.enums.MethodType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Action {

    /**
     * 访问该的值
     * 
     * @return
     */
    String value();

    /**
     * 页面请求类型，默认为接受所有的请求类型
     * 
     * @return
     */
    MethodType methodType() default MethodType.ALL;

}
