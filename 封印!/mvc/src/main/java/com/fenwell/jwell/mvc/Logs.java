package com.fenwell.jwell.mvc;

import com.fenwell.jwell.mvc.api.Logger;
import com.fenwell.jwell.mvc.spi.logger.AdapterLogger;

public class Logs {

    public static final String LOGGER_PRE = "jwell-mvc -> ";

    private static final String NAME = "JWell logger";

    public static Logger getLogger(Class<?> cls) {
        String name = NAME;
        if (cls != null) {
            name = cls.getName();
        }
        return getLogger(name);
    }

    public static Logger getLogger() {
        String name = LOGGER_PRE;
        return getLogger(name);
    }

    public static Logger getLogger(String name) {
        Logger logger = null;
        try {
            logger = Bootstrap.load(Logger.class);
        } catch (Exception e) {
            // skip
        }
        if (logger == null) {
            logger = new AdapterLogger(name);
        }
        return logger;
    }

}