package com.fenwell.jwell.mvc.spi.logger;

import com.fenwell.jwell.mvc.api.Logger;

public class JdkLogger implements Logger {

    private java.util.logging.Logger logger;

    public JdkLogger(String name) {
        logger = java.util.logging.Logger.getLogger(name);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void info(String msg, Throwable t) {
    }

    public void infof(String msg, Object... obj) {
    }

    public void warn(String msg) {
        // TODO Auto-generated method stub
        
    }

    public void warn(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void warn(String msg, Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void warnf(String msg, Object... obj) {
        // TODO Auto-generated method stub
        
    }

    public void error(String msg) {
        // TODO Auto-generated method stub
        
    }

    public void error(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void error(String msg, Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void errorf(String msg, Object... obj) {
        // TODO Auto-generated method stub
        
    }

    public void debug(String msg) {
        // TODO Auto-generated method stub
        
    }

    public void debug(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void debug(String msg, Throwable t) {
        // TODO Auto-generated method stub
        
    }

    public void debugf(String msg, Object... obj) {
        // TODO Auto-generated method stub
        
    }


}
