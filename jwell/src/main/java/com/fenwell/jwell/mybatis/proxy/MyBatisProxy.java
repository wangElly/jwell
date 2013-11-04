package com.fenwell.jwell.mybatis.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fenwell.jwell.mybatis.annotation.SQL;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MyBatisProxy implements MethodInterceptor {

    public Object intercept(Object target, Method mtd, Object[] args, MethodProxy proxy)
            throws Throwable {
        if (!isDaoMethod(mtd)) {
            System.out.println("执行个p呀，他就没有annotation !");
            return false;
        }
        return true;
    }

    private boolean isDaoMethod(Method mtd) {
        Annotation[] anns = mtd.getAnnotations();
        for (Annotation an : anns) {
            SQL sql = an.annotationType().getAnnotation(SQL.class);
            if (sql != null)
                return true;
        }
        return false;
    }

}
