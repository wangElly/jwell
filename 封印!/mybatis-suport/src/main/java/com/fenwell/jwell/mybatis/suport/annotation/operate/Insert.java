package com.fenwell.jwell.mybatis.suport.annotation.operate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fenwell.jwell.utils.Strings;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {

    String value() default Strings.EMPTY;

}
