package com.fenwell.jwell.mybatis.suport.spi.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.ibatis.session.SqlSessionFactory;

import com.fenwell.jwell.mybatis.suport.MyBatisTemplate;
import com.fenwell.jwell.mybatis.suport.annotation.Namespace;
import com.fenwell.jwell.mybatis.suport.annotation.Param;
import com.fenwell.jwell.mybatis.suport.annotation.operate.Delete;
import com.fenwell.jwell.mybatis.suport.annotation.operate.Insert;
import com.fenwell.jwell.mybatis.suport.annotation.operate.Select;
import com.fenwell.jwell.mybatis.suport.annotation.operate.Update;
import com.fenwell.jwell.mybatis.suport.api.BeanFactory;
import com.fenwell.jwell.utils.Arrays;
import com.fenwell.jwell.utils.Javassists;
import com.fenwell.jwell.utils.Maps;
import com.fenwell.jwell.utils.Reflects;
import com.fenwell.jwell.utils.Strings;

public class DefaultBeanFactory implements BeanFactory {

    private static final String PROXY_CLASS_FIXED_NAME = "$ProxyImpl";

    private static final Map<String, MyBatisTemplate> continer = new ConcurrentHashMap<String, MyBatisTemplate>();

    private ClassPool pool = ClassPool.getDefault();

    private Set<String> packages = new HashSet<String>();

    public DefaultBeanFactory() {
        Iterator<?> itera = pool.getImportedPackages();
        while (itera.hasNext()) {
            packages.add(itera.next().toString());
        }
    }

    @Override
    public MyBatisTemplate build(Class<?> cls, SqlSessionFactory ssf) throws Exception {
        if (cls == null) {
            throw new NullPointerException("class is null , can not build entity!");
        }
        MyBatisTemplate tmp = continer.get(cls.getName());
        if (tmp != null) {
            return tmp;
        }
        if (ssf == null) {
            throw new NullPointerException("SQLSessionFactory is null ,can't create transaction !");
        }
        if (!cls.isInterface()) {
            throw new IllegalAccessException(cls.getName() + " is't interface !");
        }
        String clsName = makeClsName(cls);
        CtClass ctCls = null;
        try {
            ctCls = pool.get(clsName);
        } catch (Exception e) {
            ctCls = makeCtClass(cls);
        }
        Class<?> ncls = ctCls.toClass();
        tmp = (MyBatisTemplate) ncls.newInstance();
        setSessionFactory(tmp, ssf);
        continer.put(cls.getName(), tmp);
        return tmp;
    }

    private CtClass makeCtClass(Class<?> cls) throws Exception {
        String clsName = makeClsName(cls);
        CtClass ctCls = pool.makeClass(clsName);
        ctCls.setSuperclass(getCtCls(MyBatisTemplate.class));
        ctCls.setInterfaces(new CtClass[] { getCtCls(cls) });
        Method[] mtds = cls.getMethods();
        for (Method mtd : mtds) {
            CtMethod ctMtd = makeMethod(ctCls, cls, mtd);
            ctCls.addMethod(ctMtd);
        }
        return ctCls;
    }

    private CtMethod makeMethod(CtClass declaring, Class<?> cls, Method mtd)
            throws CannotCompileException, Exception {
        Annotation[] annos = mtd.getAnnotations();
        CtMethod ctMtd = null;
        if (Arrays.isEmpty(annos)) {
            ctMtd = emptyMethod(mtd, declaring);
        } else {
            importPackage(mtd);
            Namespace namespace = cls.getAnnotation(Namespace.class);
            Insert insert = mtd.getAnnotation(Insert.class);
            Delete delete = mtd.getAnnotation(Delete.class);
            Update update = mtd.getAnnotation(Update.class);
            Select select = mtd.getAnnotation(Select.class);
            if (insert != null) {
                ctMtd = insertMethod(namespace, insert, mtd, cls, declaring);
            } else if (delete != null) {
                ctMtd = deleteMethod(namespace, delete, mtd, cls, declaring);
            } else if (update != null) {
                ctMtd = updateMethod(namespace, update, mtd, cls, declaring);
            } else if (select != null) {
                ctMtd = selectMethod(namespace, select, mtd, cls, declaring);
            } else {
                ctMtd = emptyMethod(mtd, declaring);
            }
        }
        return ctMtd;
    }

    private void importPackage(Method mtd) {
        importPackage(Map.class);
        importPackage(HashMap.class);
        importPackage(List.class);
        importPackage(ArrayList.class);
        Class<?> retType = mtd.getReturnType();
        if (!retType.getName().equals("void")) {
            importPackage(retType);
        }
        Class<?>[] paramCls = mtd.getParameterTypes();
        if (!Arrays.isEmpty(paramCls)) {
            for (Class<?> cls : paramCls) {
                importPackage(cls);
            }
        }
    }

    private void importPackage(Class<?> cls) {
        if (cls.isPrimitive()) {
            return;
        }
        importPackage(cls.getName());
    }

    private void importPackage(String cls) {
        if (packages.contains(cls)) {
            return;
        }
        pool.importPackage(cls);
        packages.add(cls);
    }

    private CtMethod insertMethod(Namespace namespace, Insert insert, Method mtd, Class<?> cls,
            CtClass declaring) throws Exception {
        String id = makeMyBatisId(namespace, insert.value(), cls, mtd);
        CtMethod ctMtd = createMethod(mtd, declaring);
        StringBuffer code = new StringBuffer(32);
        code.append("{");
        code.append(makeParameter(mtd));
        code.append("int result$value = super.save(\"").append(id).append("\",");
        code.append("param$value);");
        code.append(makeResult(mtd)).append("}");
        ctMtd.setBody(code.toString());
        return ctMtd;
    }

    private CtMethod deleteMethod(Namespace namespace, Delete insert, Method mtd, Class<?> cls,
            CtClass declaring) throws Exception {
        String id = makeMyBatisId(namespace, insert.value(), cls, mtd);
        CtMethod ctMtd = createMethod(mtd, declaring);
        StringBuffer code = new StringBuffer(32);
        code.append("{\r");
        code.append(makeParameter(mtd));
        code.append("int result$value = super.delete(\"").append(id).append("\",");
        code.append("param$value);\r");
        code.append(makeResult(mtd)).append("\r}");
        ctMtd.setBody(code.toString());
        return ctMtd;
    }

    private CtMethod updateMethod(Namespace namespace, Update insert, Method mtd, Class<?> cls,
            CtClass declaring) throws Exception {
        String id = makeMyBatisId(namespace, insert.value(), cls, mtd);
        CtMethod ctMtd = createMethod(mtd, declaring);
        StringBuffer code = new StringBuffer(32);
        code.append("{");
        code.append(makeParameter(mtd));
        code.append("int result$value = super.update(\"").append(id).append("\",");
        code.append("param$value);");
        code.append(makeResult(mtd)).append("}");
        ctMtd.setBody(code.toString());
        return ctMtd;
    }

    private CtMethod selectMethod(Namespace namespace, Select select, Method mtd, Class<?> cls,
            CtClass declaring) throws Exception {
        String id = makeMyBatisId(namespace, select.value(), cls, mtd);
        CtMethod ctMtd = createMethod(mtd, declaring);
        StringBuffer code = new StringBuffer(32);
        code.append("{\r");
        code.append(makeParameter(mtd));
        Class<?> retType = mtd.getReturnType();
        if (!retType.toString().equals("void")) {
            if (retType.isAssignableFrom(List.class)) {
                code.append("return super.selectList(\"").append(id).append("\",");
                code.append("param$value);\r");
            } else {
                code.append("return (").append(retType.getName()).append(")");
                code.append("super.selectOne(\"").append(id).append("\",");
                code.append("param$value);\r");
            }
        }
        code.append("}");
        ctMtd.setBody(code.toString());
        return ctMtd;
    }

    private String makeResult(Method mtd) {
        Class<?> retType = mtd.getReturnType();
        if (retType.toString().equals("void")) {
            return Strings.EMPTY;
        }
        String ret = Strings.EMPTY;
        if (retType.equals(int.class) || retType.equals(Integer.class)) {
            ret = "return result$value;";
        } else if (retType.equals(boolean.class) || retType.equals(Boolean.class)) {
            ret = "return result$value > 0;";
        } else {
            ret = "return null;";
        }
        return ret;
    }

    private String makeParameter(Method mtd) {
        Class<?>[] paramCls = mtd.getParameterTypes();
        if (Arrays.isEmpty(paramCls)) {
            return "Object param$value = null;\r";
        } else if (paramCls.length == 1) {
            Class<?> cls = paramCls[0];
            if (cls.isPrimitive()) {
                return "Object param$value = " + Javassists.primitive2Wrap(cls, 1) + ";\r";
            } else {
                return "Object param$value = $1;\r";
            }
        } else {
            Annotation[][] annos = mtd.getParameterAnnotations();
            if (Arrays.isEmpty(annos)) {
                return "Object param$value = null;\r";
            }
            Map<Integer, Param> paramMap = null;
            for (int j = 0; j < annos.length; j++) {
                Annotation[] anns = annos[j];
                for (int i = 0; i < anns.length; i++) {
                    Annotation ann = anns[i];
                    if (ann instanceof Param) {
                        if (paramMap == null) {
                            paramMap = new HashMap<Integer, Param>();
                        }
                        paramMap.put(j + 1, (Param) ann);
                    }
                }
            }
            if (Maps.isEmpty(paramMap)) {
                return "Object param$value = null;\r";
            } else {
                StringBuffer code = new StringBuffer(32);
                code.append("Map param$value = new HashMap();\r");
                for (Integer id : paramMap.keySet()) {
                    Param p = paramMap.get(id);
                    Class<?> cls = mtd.getParameterTypes()[id - 1];
                    code.append("param$value.put(\"").append(p.value()).append("\",");
                    if (cls.isPrimitive()) {
                        code.append(Javassists.primitive2Wrap(cls, id));
                    } else {
                        code.append("$").append(id);
                    }
                    code.append(");\r");
                }
                return code.toString();
            }
        }
    }


    private String makeMyBatisId(Namespace namespace, String value, Class<?> cls, Method mtd) {
        String id = Strings.EMPTY;
        if (namespace != null) {
            if (Strings.isBlank(namespace.value())) {
                id = cls.getSimpleName();
            } else {
                id = namespace.value();
            }
            id += ".";
        }
        if (Strings.isBlank(value)) {
            id += mtd.getName();
        } else {
            id += value;
        }
        return id;
    }

    private CtMethod emptyMethod(Method m, CtClass declaring) throws Exception,
            CannotCompileException {
        CtMethod ctMethod = createMethod(m, declaring);
        Class<?> retType = m.getReturnType();
        if (retType.toString().equals("void")) {
            ctMethod.setBody("{}");
        } else {
            String ret = Reflects.convertDefault(retType);
            StringBuffer code = new StringBuffer(32);
            code.append("{return ").append(ret).append(";}");
            ctMethod.setBody(code.toString());
        }
        return ctMethod;
    }

    private CtMethod createMethod(Method m, CtClass declaring) throws Exception {
        CtClass returnType = pool.get(m.getReturnType().getName());
        String mname = m.getName();
        CtClass[] parameters = null;
        Class<?>[] oldParam = m.getParameterTypes();
        if (oldParam != null) {
            parameters = new CtClass[oldParam.length];
            for (int i = 0; i < oldParam.length; i++) {
                parameters[i] = pool.get(oldParam[i].getName());
            }
        }
        CtMethod ctMethod = new CtMethod(returnType, mname, parameters, declaring);
        return ctMethod;
    }

    private void setSessionFactory(MyBatisTemplate tmpl, SqlSessionFactory ssf) {
        tmpl.setSessionFactory(ssf);
    }

    private CtClass getCtCls(Class<?> cls) throws NotFoundException {
        return pool.get(cls.getName());
    }

    private String makeClsName(Class<?> cls) {
        return cls.getName() + PROXY_CLASS_FIXED_NAME;
    }

}
