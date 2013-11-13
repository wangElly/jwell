package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;

import com.fenwell.jwell.mybatis.transaction.Tran;

public class UpdateOperation extends AbstractOperation {

    @Override
    public Object execute(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        int result = session.update(id, param);
        Tran.closeSession(session);
        if (returnTypeIsVoid(mtd)) {
            return null;
        }
        if (returnTypeIsPrimitive(mtd)) {
            Class<?> cls = mtd.getReturnType();
            return returnPrimitiveType(cls, result);
        }
        return null;
    }

}
