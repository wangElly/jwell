package com.fenwell.jwell.mybatis.suport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fenwell.jwell.utils.Strings;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Namespace {

    String value() default Strings.EMPTY;

}
