package com.fenwell.jwell.spi.pojo;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fenwell.jwell.annotation.Param;
import com.fenwell.jwell.api.Interceptors;
import com.fenwell.jwell.enums.MethodType;

public class ActionContent {

    private Class<?>[] paramClass;

    private Param[] paramAnnotation;

    private int methodIndex;

    private MethodAccess method;

    private Class<? extends Interceptors>[] interceptors;

    private MethodType methodType;

    private Object instance;

    public Class<?>[] getParamClass() {
        return paramClass;
    }

    public void setParamClass(Class<?>[] paramClass) {
        this.paramClass = paramClass;
    }

    public Param[] getParamAnnotation() {
        return paramAnnotation;
    }

    public void setParamAnnotation(Param[] paramAnnotation) {
        this.paramAnnotation = paramAnnotation;
    }

    public MethodAccess getMethod() {
        return method;
    }

    public void setMethod(MethodAccess mtd) {
        this.method = mtd;
    }

    public Class<? extends Interceptors>[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class<? extends Interceptors>[] interceptors) {
        this.interceptors = interceptors;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public int getMethodIndex() {
        return methodIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }

}
