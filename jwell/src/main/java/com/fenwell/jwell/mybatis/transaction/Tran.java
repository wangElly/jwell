package com.fenwell.jwell.mybatis.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class Tran {

    private static final Log log = LogFactory.getLog(Tran.class);
    private static final ThreadLocal<SqlSession> sessionHolder = new ThreadLocal<SqlSession>();
    private static final ThreadLocal<Boolean> isTransaction = new ThreadLocal<Boolean>();

    private static SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("unchecked")
    public static <T> T tran(Atom atom) {
        Object result = null;
        // 开启事务！
        isTransaction.set(true);
        SqlSession session = getSession();
        try {
            result = atom.execute();
            session.commit();
        } catch (Throwable t) {
            session.rollback();
        } finally {
            session.close();
            sessionHolder.remove();
            isTransaction.remove();
        }
        return result == null ? null : ((T) result);
    }

    public static SqlSession getSession() {
        SqlSession session = null;
        if (isTran()) {
            session = sessionHolder.get();
            if (session == null) {
                session = sqlSessionFactory.openSession();
                sessionHolder.set(session);
            }
        } else {
            session = sqlSessionFactory.openSession();
        }
        return session;
    }

    public static void closeSession(SqlSession session) {
        if (!isTran()) {
            session.commit();
            session.close();
        }
    }

    private static boolean isTran() {
        return isTransaction.get() == null ? false : isTransaction.get();
    }

    public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (Tran.sqlSessionFactory == null) {
            Tran.sqlSessionFactory = sqlSessionFactory;
        }
    }

}
