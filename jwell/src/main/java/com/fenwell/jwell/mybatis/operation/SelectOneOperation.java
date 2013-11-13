package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;

import com.fenwell.jwell.mybatis.transaction.Tran;

public class SelectOneOperation extends AbstractOperation {

    @Override
    public Object execute(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        Object result = session.selectOne(id, param);
        Tran.closeSession(session);
        if (returnTypeIsVoid(mtd)) {
            return null;
        }
        return result;
    }

}
