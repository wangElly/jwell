package com.fenwell.jwell.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fenwell.util.Strings;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Namespace {

    String id() default Strings.EMPTY;

}
