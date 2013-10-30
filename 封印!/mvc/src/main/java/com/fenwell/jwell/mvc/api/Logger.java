package com.fenwell.jwell.mvc.api;

public interface Logger {

    public void info(String msg);

    public void info(Throwable t);

    public void info(String msg, Throwable t);

    public void infof(String msg, Object... obj);

    public void warn(String msg);

    public void warn(Throwable t);

    public void warn(String msg, Throwable t);

    public void warnf(String msg, Object... obj);

    public void error(String msg);

    public void error(Throwable t);

    public void error(String msg, Throwable t);

    public void errorf(String msg, Object... obj);

    public void debug(String msg);

    public void debug(Throwable t);

    public void debug(String msg, Throwable t);

    public void debugf(String msg, Object... obj);
    
    public boolean isDebugEnabled();
    
}
