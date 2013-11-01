package com.fenwell.jwell;

import javax.servlet.FilterConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.fenwell.util.Strings;

public class Mvcs {

    private static final Log log = LogFactory.getLog(Mvcs.class);

    private static Mvcs instance = new Mvcs();

    private ApplicationContext applicationContext;

    private FilterConfig filterConfig;

    private String suffix = ".do";

    private Mvcs() {
    }

    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    public void setFilterConfig(FilterConfig conf) {
        this.filterConfig = conf;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public static Mvcs getInstance() {
        return instance;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static FilterConfig getFilterConfig() {
        return instance.filterConfig;
    }

    public static String getSuffix() {
        return instance.suffix;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        if (instance.applicationContext == null) {
            log.warn("ApplicationContext is null , please initial ApplicationContext");
            return null;
        }
        if (Strings.isBlank(name)) {
            log.warn("Cannot create bean ,bean name is empty !");
            return null;
        }
        return (T) instance.applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> cls) {
        if (instance.applicationContext == null) {
            log.warn("ApplicationContext is null , please initial ApplicationContext");
            return null;
        }
        if (cls == null) {
            log.warn("Cannot create bean ,class name is empty !");
            return null;
        }
        return instance.applicationContext.getBean(cls);
    }

}
