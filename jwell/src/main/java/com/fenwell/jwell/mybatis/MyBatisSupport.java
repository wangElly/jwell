package com.fenwell.jwell.mybatis;

import net.sf.cglib.proxy.Enhancer;

import com.fenwell.jwell.mybatis.proxy.MyBatisProxy;

public class MyBatisSupport {

    @SuppressWarnings("unchecked")
    public static final <T> T craeteDao(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new MyBatisProxy());
        return (T) enhancer.create();
    }

}