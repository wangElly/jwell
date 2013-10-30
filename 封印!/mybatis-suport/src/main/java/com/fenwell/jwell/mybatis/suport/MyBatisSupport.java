package com.fenwell.jwell.mybatis.suport;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.fenwell.jwell.mybatis.suport.api.BeanFactory;
import com.fenwell.jwell.mybatis.suport.spi.factory.DefaultBeanFactory;

public class MyBatisSupport {

    private SqlSessionFactory sessionFactory;

    private BeanFactory factory = new DefaultBeanFactory();

    public MyBatisSupport(Configuration conf) {
        this.sessionFactory = new SqlSessionFactoryBuilder().build(conf);
    }

    public MyBatisSupport(String confPath) {
        this.sessionFactory = createSessionFactory(confPath);
    }

    @SuppressWarnings("unchecked")
    public <T> T getDaoBean(Class<T> t) {
        T obj = null;
        try {
            obj = (T) factory.build(t, sessionFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private SqlSessionFactory createSessionFactory(String path) {
        SqlSessionFactory ssf = null;
        try {
            InputStream is = Resources.getResourceAsStream(path);
            ssf = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ssf;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

}
