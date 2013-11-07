package com.fenwell.jwell.mybatis.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.mybatis.annotation.SQL;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MyBatisProxy implements MethodInterceptor {

    private static final Log log = LogFactory.getLog(MyBatisProxy.class);

    public Object intercept(Object target, Method mtd, Object[] args, MethodProxy proxy)
            throws Throwable {
        if (!isDaoMethod(mtd)) {
            log.warn(mtd + " is not dao method !");
            return null;
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
