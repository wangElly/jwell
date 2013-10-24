package com.fenwell.jwell.mvc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import com.fenwell.jwell.utils.Strings;

public final class Mvcs {

    public static final String CONFIG_LOADER = "loader";
    public static final String CONFIG_MVC = "mvc";

    private static FilterConfig filterConfig;

    private static final Map<String, Config> configs = new ConcurrentHashMap<String, Config>();

    private Mvcs() {
    }

    private static final Mvcs mvcContext = new Mvcs();
    
    public final static Mvcs getInstance() {
        return mvcContext;
    }

    public final static FilterConfig filterConfig() {
        return filterConfig;
    }

    public final static void setFilterConfig(FilterConfig fc) {
        filterConfig = fc;
    }
    
    public final static ServletContext servletContext(){
        return filterConfig.getServletContext();
    }

    public final static Config get(String key) {
        return configs.get(key);
    }

    public void set(String key, Config cfg) {
        if (Strings.isBlank(key) || cfg == null) {
            return;
        }
        Config oraCfg = configs.get(key);
        if (oraCfg == null) {
            configs.put(key, cfg);
            return;
        }
        oraCfg.put(cfg);
    }

}
