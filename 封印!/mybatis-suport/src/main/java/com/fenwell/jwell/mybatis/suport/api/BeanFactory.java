package com.fenwell.jwell.mybatis.suport.api;

import org.apache.ibatis.session.SqlSessionFactory;

import com.fenwell.jwell.mybatis.suport.MyBatisTemplate;

public interface BeanFactory {

    public MyBatisTemplate build(Class<?> cls, SqlSessionFactory ssf) throws Exception;

}
