package com.fenwell.jwell.mybatis;

import com.fenwell.jwell.mybatis.proxy.MyBatisProxy;

import net.sf.cglib.proxy.Enhancer;

public class MyBatisSupport {

    @SuppressWarnings("unchecked")
    public static final <T> T craeteDao(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new MyBatisProxy());
        return (T) enhancer.create();
    }

}