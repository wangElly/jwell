package com.fenwell.jwell.mvc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fenwell.jwell.utils.Strings;

public final class Config {

    private final Map<String, Object> values = new ConcurrentHashMap<String, Object>();

    private Config() {
    }

    public static final Config newInstance() {
        return new Config();
    }

    public String getString(String key) {
        Object obj = values.get(key);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> t) {
        Object obj = values.get(key);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List<?>)) {
            return null;
        }
        return (List<T>) obj;
    }

    public void put(Config config) {
        if (config == null) {
            return;
        }
        for (String key : config.values.keySet()) {
            this.values.put(key, config.values.get(key));
        }
    }

    public void put(String key, Object obj) {
        if (Strings.isBlank(key) || obj == null) {
            return;
        }
        values.put(key, obj);
    }

}
