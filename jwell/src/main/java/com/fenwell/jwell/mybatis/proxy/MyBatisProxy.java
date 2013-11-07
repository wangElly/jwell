package com.fenwell.jwell.mybatis.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.mybatis.annotation.Delete;
import com.fenwell.jwell.mybatis.annotation.Insert;
import com.fenwell.jwell.mybatis.annotation.SQL;
import com.fenwell.jwell.mybatis.annotation.SelectList;
import com.fenwell.jwell.mybatis.annotation.SelectOne;
import com.fenwell.jwell.mybatis.annotation.Update;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MyBatisProxy implements MethodInterceptor {

    private static final Log log = LogFactory.getLog(MyBatisProxy.class);

    public Object intercept(Object target, Method mtd, Object[] args, MethodProxy proxy)
            throws Throwable {
        Annotation an = hasDaoMethod(mtd);
        if (an == null) {
            log.warn(mtd + " is not dao method !");
            return null;
        }
        Object result = null;
        if (an instanceof Insert) {
            System.out.println("插入");
        } else if (an instanceof Delete) {
            System.out.println("删除");
        } else if (an instanceof Update) {
            System.out.println("修改");
        } else if (an instanceof SelectOne) {
            System.out.println("查询一个");
        } else if (an instanceof SelectList) {
            System.out.println("查询列表！");
        }
        return result;
    }

    private Annotation hasDaoMethod(Method mtd) {
        Annotation[] anns = mtd.getAnnotations();
        for (Annotation an : anns) {
            SQL sql = an.annotationType().getAnnotation(SQL.class);
            if (sql != null)
                return an;
        }
        return null;
    }

}
