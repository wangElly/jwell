package com.fenwell.jwell.mybatis.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.fenwell.jwell.mybatis.annotation.Delete;
import com.fenwell.jwell.mybatis.annotation.Insert;
import com.fenwell.jwell.mybatis.annotation.Namespace;
import com.fenwell.jwell.mybatis.annotation.SQL;
import com.fenwell.jwell.mybatis.annotation.SelectList;
import com.fenwell.jwell.mybatis.annotation.SelectOne;
import com.fenwell.jwell.mybatis.annotation.Update;
import com.fenwell.jwell.mybatis.transaction.Tran;
import com.fenwell.util.Strings;

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
        Object param = makeParam();
        String id = getId(an, target, mtd);
        if (an instanceof Insert) {
            result = doInsert(id, mtd, param);
        } else if (an instanceof Delete) {
            result = doDelete(id, mtd, param);
        } else if (an instanceof Update) {
            result = doUpdate(id, mtd, param);
        } else if (an instanceof SelectOne) {
            System.out.println("查询一个");
        } else if (an instanceof SelectList) {
            System.out.println("查询列表！");
        }
        return result;
    }

    private String getId(Annotation an, Object target, Method mtd) {
        String id = makeId(an, target, mtd);
        return id;
    }

    private String makeId(Annotation an, Object target, Method mtd) {
        Class<?> cls = target.getClass().getInterfaces()[0];
        Namespace namespace = cls.getAnnotation(Namespace.class);
        String ns = Strings.EMPTY;
        if (namespace != null) {
            if (Strings.isBlank(namespace.id())) {
                ns = cls.getName();
            } else {
                ns = namespace.id();
            }
        }
        String id = Strings.EMPTY;
        if (an instanceof Insert) {
            id = ((Insert) an).id();
        } else if (an instanceof Delete) {
            id = ((Delete) an).id();
        } else if (an instanceof Update) {
            id = ((Update) an).id();
        } else if (an instanceof SelectOne) {
            id = ((SelectOne) an).id();
        } else if (an instanceof SelectList) {
            id = ((SelectList) an).id();
        }
        if (Strings.isBlank(id)) {
            id = mtd.getName();
        }
        return Strings.isEmpty(ns) ? id : ns + "." + id;
    }

    private Map<String, Object> makeParam() {
        return null;
    }

    /**
     * 执行insert 方法
     * 
     * @param id
     * @param mtd
     * @param param
     * @return
     */
    private Object doInsert(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        boolean result = session.insert(id, param) > 0;
        Tran.closeSession(session);
        return result;
    }

    private Object doUpdate(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        boolean result = session.update(id, param) > 0;
        Tran.closeSession(session);
        return result;
    }

    private Object doDelete(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        boolean result = session.delete(id, param) > 0;
        Tran.closeSession(session);
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
