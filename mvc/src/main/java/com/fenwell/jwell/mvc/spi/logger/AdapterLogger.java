package com.fenwell.jwell.mvc.spi.logger;

import com.fenwell.jwell.mvc.api.Logger;

public class AdapterLogger implements Logger {

    private static Logger logger;

    public AdapterLogger(String name) {
        logger = adapter(name);
    }

    private Logger adapter(String name) {
        if (logger != null) {
            return logger;
        }
        Logger logger = null;
        try {
            logger = new Log4jLogger(name);
            logger.info("Using log4j logger for jwell ");
        } catch (Throwable e) {
            try {
                logger = new SimpleLogger(name);
                logger.info("Using simple logger for jwell ");
            } catch (Throwable e1) {
                logger = new JdkLogger(name);
                logger.info("Using jdk logger for jwell ");
            }
        }
        return logger;
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(Throwable t) {
        logger.info(t);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public void infof(String msg, Object... obj) {
        logger.infof(msg, obj);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(Throwable t) {
        logger.warn(t);
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public void warnf(String msg, Object... obj) {
        logger.warnf(msg, obj);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(Throwable t) {
        logger.error(t);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public void errorf(String msg, Object... obj) {
        logger.errorf(msg, obj);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(Throwable t) {
        logger.debug(t);
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public void debugf(String msg, Object... obj) {
        logger.debugf(msg, obj);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

}
