package com.fenwell.jwell.mybatis;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import net.sf.cglib.proxy.Enhancer;

import com.fenwell.jwell.mybatis.proxy.MyBatisProxy;
import com.fenwell.jwell.mybatis.transaction.Tran;

public class MyBatisSupport {

    private static final Log log = LogFactory.getLog(MyBatisSupport.class);

    private static SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("unchecked")
    public static final <T> T craeteDao(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new MyBatisProxy());
        return (T) enhancer.create();
    }

    private static void makeSqlSessionFactory(String conf) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(conf);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            Tran.setSqlSessionFactory(sqlSessionFactory);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void setConfiguration(String configuration) {
        makeSqlSessionFactory(configuration);
    }

}