package com.fenwell.jwell.mybatis;

import net.sf.cglib.proxy.Enhancer;

import com.fenwell.jwell.mybatis.proxy.MyBatisProxy;

public class MyBatisSupport {

    private String configuration;

    @SuppressWarnings("unchecked")
    public static final <T> T craeteDao(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new MyBatisProxy());
        return (T) enhancer.create();
    }

    public void init() {
        System.out.println(configuration);
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

}