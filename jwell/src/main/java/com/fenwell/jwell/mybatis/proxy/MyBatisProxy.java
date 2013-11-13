package com.fenwell.jwell.mybatis.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.mybatis.annotation.Delete;
import com.fenwell.jwell.mybatis.annotation.Insert;
import com.fenwell.jwell.mybatis.annotation.Key;
import com.fenwell.jwell.mybatis.annotation.Namespace;
import com.fenwell.jwell.mybatis.annotation.SQL;
import com.fenwell.jwell.mybatis.annotation.SelectList;
import com.fenwell.jwell.mybatis.annotation.SelectOne;
import com.fenwell.jwell.mybatis.annotation.Update;
import com.fenwell.jwell.mybatis.operation.AbstractOperation;
import com.fenwell.jwell.mybatis.operation.DeleteOperation;
import com.fenwell.jwell.mybatis.operation.InsertOperation;
import com.fenwell.jwell.mybatis.operation.SelectListOperation;
import com.fenwell.jwell.mybatis.operation.SelectOneOperation;
import com.fenwell.jwell.mybatis.operation.UpdateOperation;
import com.fenwell.util.Arrays;
import com.fenwell.util.Maps;
import com.fenwell.util.Strings;

public class MyBatisProxy implements MethodInterceptor {

    private static final Log log = LogFactory.getLog(MyBatisProxy.class);

    private AbstractOperation insert = new InsertOperation();
    private AbstractOperation delete = new DeleteOperation();
    private AbstractOperation update = new UpdateOperation();
    private AbstractOperation selectOne = new SelectOneOperation();
    private AbstractOperation selectList = new SelectListOperation();

    public Object intercept(Object target, Method mtd, Object[] args, MethodProxy proxy)
            throws Throwable {
        Annotation an = hasDaoMethod(mtd);
        if (an == null) {
            log.warn(mtd + " is not dao method !");
            return null;
        }
        Object param = makeParam(mtd, args);
        String id = getId(an, target, mtd);
        AbstractOperation operation = operationFactory(an);
        return operation.execute(id, mtd, param);
    }

    private AbstractOperation operationFactory(Annotation an) {
        AbstractOperation operation = null;
        if (an instanceof Insert) {
            operation = insert;
        } else if (an instanceof Delete) {
            operation = delete;
        } else if (an instanceof Update) {
            operation = update;
        } else if (an instanceof SelectOne) {
            operation = selectOne;
        } else if (an instanceof SelectList) {
            operation = selectList;
        }
        return operation;
    }

    private String getId(Annotation an, Object target, Method mtd) {
        // TODO 以后需要缓存ID 。免得每次都用反射获取。
        return makeId(an, target, mtd);
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

    private Object makeParam(Method mtd, Object[] args) {
        // TODO 以后也需要缓存。。 加批注
        if (Arrays.isEmpty(args)) {
            return null;
        }
        Annotation[][] anns = mtd.getParameterAnnotations();
        Map<String, Object> param = new HashMap<String, Object>();
        for (int i = 0; i < anns.length; i++) {
            for (int j = 0; j < anns[i].length; j++) {
                if (anns[i][j] instanceof Key) {
                    Key key = (Key) anns[i][j];
                    param.put(key.value(), args[i]);
                }
            }
        }
        if (Maps.isEmpty(param)) {
            return null;
        }
        if (param.size() == 1) {
            List<Object> list = new ArrayList<Object>(param.values());
            return list.get(0);
        }
        return param;
    }

    private Annotation hasDaoMethod(Method mtd) {
        // TODO 需要缓存管理。
        Annotation[] anns = mtd.getAnnotations();
        for (Annotation an : anns) {
            SQL sql = an.annotationType().getAnnotation(SQL.class);
            if (sql != null)
                return an;
        }
        return null;
    }

}
