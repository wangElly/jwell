package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.fenwell.jwell.mybatis.transaction.Tran;

public class SelectListOperation extends AbstractOperation {

    @Override
    public Object execute(String id, Method mtd, Object param) {
        SqlSession session = Tran.getSession();
        List<Object> result = session.selectList(id, param);
        Tran.closeSession(session);
        if (returnTypeIsVoid(mtd)) {
            return null;
        }
        return result;
    }

}
