package com.fenwell.jwell.mybatis.transaction;

public abstract class Atom {

    public void commit() {
    }

    public void rollback() {
    }

    public abstract Object execute() throws Throwable;

}
