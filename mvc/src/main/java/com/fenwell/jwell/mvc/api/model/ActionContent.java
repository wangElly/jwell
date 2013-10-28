package com.fenwell.jwell.mvc.api.model;

import java.lang.reflect.Method;

import com.fenwell.jwell.mvc.api.Interceptor;
import com.fenwell.jwell.mvc.api.annotation.Param;
import com.fenwell.jwell.mvc.api.enums.MethodType;

public class ActionContent {

    private String uri;

    private Class<?>[] paramClass;

    private Param[] paramAnnotation;

    private Method method;

    private Class<?> clasz;

    private Class<Interceptor>[] interceptors;

    private MethodType methodType;

    private Object instance;

    public Class<?>[] getParamClass() {
        return paramClass;
    }

    public void setParamClass(Class<?>[] paramClass) {
        this.paramClass = paramClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClasz() {
        return clasz;
    }

    public void setClasz(Class<?> clasz) {
        this.clasz = clasz;
    }

    public Class<Interceptor>[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class<Interceptor>[] interceptors) {
        this.interceptors = interceptors;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Param[] getParamAnnotation() {
        return paramAnnotation;
    }

    public void setParamAnnotation(Param[] paramAnnotation) {
        this.paramAnnotation = paramAnnotation;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Object instance() {
        return instance;
    }

}
