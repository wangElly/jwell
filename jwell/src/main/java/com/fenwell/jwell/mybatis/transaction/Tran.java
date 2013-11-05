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
        }
        closeSession(session);
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

    private static boolean isTran() {
        return isTransaction.get() == null ? false : isTransaction.get();
    }

    public static void closeSession(SqlSession session) {
        if (session == null) {
            log.warn("session is null ,cannot close this !");
            return;
        }
        session.commit();
        session.close();
        
        if (!isTran()) {
            // 关闭事务
            sessionHolder.remove();
            isTransaction.remove();
        }
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        Tran.sqlSessionFactory = sqlSessionFactory;
    }

}
