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
import com.fenwell.jwell.mybatis.proxy.vo.ParamVO;
import com.fenwell.util.Arrays;
import com.fenwell.util.Collections;
import com.fenwell.util.Strings;

public class MyBatisProxy implements MethodInterceptor {

    private static final Log log = LogFactory.getLog(MyBatisProxy.class);

    private AbstractOperation insert = new InsertOperation();
    private AbstractOperation delete = new DeleteOperation();
    private AbstractOperation update = new UpdateOperation();
    private AbstractOperation selectOne = new SelectOneOperation();
    private AbstractOperation selectList = new SelectListOperation();

    private Map<Method, String> ID_CACHE = new HashMap<Method, String>();
    private Map<Method, Annotation> ANNOTATION_CACHE = new HashMap<Method, Annotation>();
    private Map<Method, ParamVO> PARAM_CACHE = new HashMap<Method, ParamVO>();

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
        String id = ID_CACHE.get(mtd);
        if (id == null) {
            id = makeId(an, target, mtd);
            ID_CACHE.put(mtd, id);
        }
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

    private Object makeParam(Method mtd, Object[] args) {
        if (Arrays.isEmpty(args)) {
            return null;
        }
        ParamVO pvo = PARAM_CACHE.get(mtd);
        Object result = null;
        if (pvo == null) {
            pvo = createParamVO(mtd, args);
            PARAM_CACHE.put(mtd, pvo);
        }
        if (pvo.isEmpty()) {
            result = null;
        } else if (pvo.isSingle()) {
            result = args[0];
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            int[] index = pvo.getIndex();
            String[] value = pvo.getValue();
            for (int i = 0; i < index.length; i++) {
                param.put(value[i], args[index[i]]);
            }
            result = param;
        }
        return result;
    }

    private ParamVO createParamVO(Method mtd, Object[] args) {
        ParamVO pvo = new ParamVO();
        Annotation[][] anns = mtd.getParameterAnnotations();
        List<Integer> index = new ArrayList<Integer>();
        List<String> value = new ArrayList<String>();
        for (int i = 0; i < anns.length; i++) {
            for (int j = 0; j < anns[i].length; j++) {
                if (anns[i][j] instanceof Key) {
                    Key key = (Key) anns[i][j];
                    value.add(key.value());
                    index.add(i);
                }
            }
        }
        if (Collections.isEmpty(index)) {
            pvo.setEmpty(true);
        } else {
            if (index.size() == 1) {
                pvo.setSingle(true);
            } else {
                pvo.setIndex(index);
                pvo.setValue(value);
            }
        }
        return pvo;
    }

    private Annotation hasDaoMethod(Method mtd) {
        Annotation temp = ANNOTATION_CACHE.get(mtd);
        if (temp == null) {
            Annotation[] anns = mtd.getAnnotations();
            for (Annotation an : anns) {
                SQL sql = an.annotationType().getAnnotation(SQL.class);
                if (sql != null)
                    temp = an;
            }
            ANNOTATION_CACHE.put(mtd, temp);
        }
        return temp;
    }

}
