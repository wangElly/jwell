package com.fenwell.jwell.mybatis.transaction;

import org.apache.ibatis.session.SqlSession;

public abstract class Atom {

    public void commit() {
        SqlSession session = Tran.getSession();
        session.commit();
    }

    public void rollback() {
        SqlSession session = Tran.getSession();
        session.rollback();
    }

    public abstract Object execute() throws Throwable;

}
