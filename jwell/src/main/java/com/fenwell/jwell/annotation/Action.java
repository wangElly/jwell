package com.fenwell.jwell.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import com.fenwell.jwell.api.Interceptors;
import com.fenwell.jwell.enums.MethodType;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Action {

    String path() default "";

    MethodType type() default MethodType.ALL;

    Class<? extends Interceptors>[] interceptors() default {};

    boolean useParentInterceptor() default true;

}
