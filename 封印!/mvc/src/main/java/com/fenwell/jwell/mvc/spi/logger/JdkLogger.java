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
        logger.info(t.getMessage());
    }

    public void info(String msg, Throwable t) {
        logger.info(msg);
    }

    public void infof(String msg, Object... obj) {
        logger.info(msg);
    }

    public void warn(String msg) {
        logger.warning(msg);
    }

    public void warn(Throwable t) {
        logger.warning(t.getMessage());
    }

    public void warn(String msg, Throwable t) {
        logger.warning(msg);
    }

    public void warnf(String msg, Object... obj) {
        logger.warning(msg);
    }

    public void error(String msg) {
        logger.warning(msg);
    }

    public void error(Throwable t) {
        logger.warning(t.getMessage());
    }

    public void error(String msg, Throwable t) {
        logger.warning(msg);
    }

    public void errorf(String msg, Object... obj) {
        logger.warning(msg);
    }

    public void debug(String msg) {
        logger.warning(msg);
    }

    public void debug(Throwable t) {
        logger.warning(t.getMessage());
    }

    public void debug(String msg, Throwable t) {
        logger.warning(msg);
    }

    public void debugf(String msg, Object... obj) {
        logger.warning(msg);
    }

    public boolean isDebugEnabled() {
        return true;
    }

}
