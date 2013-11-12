package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;

public abstract class AbstractOperation {

    public abstract Object execute(String id, Method mtd, Object param);

}
