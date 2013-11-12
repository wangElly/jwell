package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;

import com.fenwell.jwell.mybatis.transaction.Tran;

public class InsertOperation extends AbstractOperation {

    @Override
    public Object execute(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        int result = session.insert(id, param);
        Tran.closeSession(session);
        if (returnTypeIsVoid(mtd)) {
            return null;
        }
        if (returnTypeIsPrimitive(mtd)) {
            Class<?> cls = mtd.getClass();
            return returnPrimitiveType(cls, result);
        }
        return null;
    }

}
