package com.fenwell.jwell.mvc.spi.logger;

import com.fenwell.jwell.mvc.api.Logger;

public class Log4jLogger implements Logger {

    private org.apache.log4j.Logger logger;

    public Log4jLogger(Class<?> cls) throws ClassNotFoundException {
        logger = org.apache.log4j.Logger.getLogger(cls);
    }

    public Log4jLogger(String name) {
        logger = org.apache.log4j.Logger.getLogger(name);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(Throwable t) {
        logger.info(null, t);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public void infof(String msg, Object... obj) {
        String str = String.format(msg, obj);
        logger.info(str);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(Throwable t) {
        logger.info(null, t);
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public void warnf(String msg, Object... obj) {
        String str = String.format(msg, obj);
        logger.warn(str);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(Throwable t) {
        logger.error(null, t);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public void errorf(String msg, Object... obj) {
        String str = String.format(msg, obj);
        logger.error(str);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(Throwable t) {
        logger.debug(null, t);
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public void debugf(String msg, Object... obj) {
        String str = String.format(msg, obj);
        logger.debug(str);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

}
