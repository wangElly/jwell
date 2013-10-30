package com.fenwell.jwell.mvc.spi.loader;

import com.fenwell.jwell.mvc.Config;
import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.Loader;

public class DefaultClassLoader implements Loader {

    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> t) {
        Config cfg = Mvcs.get(Mvcs.CONFIG_LOADER);
        String clsName = cfg.getString(t.getName());
        T inst = null;
        try {
            Class<T> cls = (Class<T>) Class.forName(clsName);
            inst = cls.newInstance();
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }
        return inst;
    }

}
