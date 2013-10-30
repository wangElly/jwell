package com.fenwell.jwell.mybatis.suport;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public abstract class MyBatisTemplate {

    public MyBatisTemplate() {
    }

    protected static final ThreadLocal<SqlSession> session = new ThreadLocal<SqlSession>();

    protected static final ThreadLocal<Boolean> isTran = new ThreadLocal<Boolean>();

    protected SqlSessionFactory sessionFactory;

    public void setSessionFactory(SqlSessionFactory ssf) {
        this.sessionFactory = ssf;
    }

    public static void setTransaction(boolean flag) {
        isTran.set(flag);
    }

    protected int save(String id, Object obj) {
        SqlSession sess = getSession();
        int result = 0;
        if (obj == null) {
            result = sess.insert(id);
        } else {
            result = sess.insert(id, obj);
        }
        commit(sess);
        return result;
    }

    protected int delete(String id, Object obj) {
        SqlSession sess = getSession();
        int result = 0;
        if (obj == null) {
            result = sess.delete(id);
        } else {
            result = sess.delete(id, obj);
        }
        commit(sess);
        return result;
    }

    protected int update(String id, Object obj) {
        SqlSession sess = getSession();
        int result = 0;
        if (obj == null) {
            result = sess.update(id);
        } else {
            result = sess.update(id, obj);
        }
        commit(sess);
        return result;
    }

    protected <T> T selectOne(String id, Object obj) {
        SqlSession sess = getSession();
        T result = null;
        if (obj == null) {
            result = sess.selectOne(id);
        } else {
            result = sess.selectOne(id, obj);
        }
        return result;
    }

    protected <T> List<T> selectList(String id, Object obj) {
        SqlSession sess = getSession();
        List<T> result = null;
        if (obj == null) {
            result = sess.selectList(id);
        } else {
            result = sess.selectList(id, obj);
        }
        return result;
    }

    public static SqlSession getCurrentSession() {
        return session.get();
    }

    public SqlSession getSession() {
        SqlSession sess = null;
        if (isTran.get() == null || !isTran.get()) {
            sess = sessionFactory.openSession();
        } else {
            sess = session.get();
            if (sess == null) {
                sess = sessionFactory.openSession();
                session.set(sess);
            }
        }
        return sess;
    }

    protected void commit(SqlSession sess) {
        if (isTran.get() == null || !isTran.get()) {
            sess.commit();
            sess.close();
        }
    }

    public void commit() {
        SqlSession sess = getSession();
        if (sess != null) {
            sess.commit();
            sess.close();
        }
    }

}
