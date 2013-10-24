package com.fenwell.jwell.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;

import com.fenwell.jwell.mvc.api.Loader;
import com.fenwell.jwell.mvc.api.Logger;
import com.fenwell.jwell.mvc.api.Resolve;
import com.fenwell.jwell.mvc.spi.resolve.JsonResolve;
import com.fenwell.jwell.utils.Strings;

public final class Bootstrap {

    private static final Logger log = Logs.getLogger();

    // 默认类
    private static final Class<? extends Resolve> DEFAULT_RESOLVE_CLASS = JsonResolve.class;
    private static Class<? extends Resolve> PROVIDER_RESOLVE_CLASS = JsonResolve.class;

    // 系统默认配置文件名称
    private static final String CONFIG_FILE_PATH = "/jwell-mvc-config.js";

    // 系统类加载器
    private static Loader LOADER;

    @SuppressWarnings("unchecked")
    public static final void startup(String fileName, String resolveName) throws ServletException {
        if (Strings.isBlank(fileName)) {
            throw new ServletException("Can not found configuration file");
        }
        try {
            if (!Strings.isBlank(resolveName)) {
                PROVIDER_RESOLVE_CLASS = (Class<Resolve>) Class.forName(resolveName);
            }
            resolve(fileName);
            init();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static final <T> T load(Class<T> t) {
        if (LOADER == null) {
            throw new NullPointerException("Loader is not initialization");
        }
        return LOADER.load(t);
    }

    private static final void resolve(String fileName) throws InstantiationException,
            IllegalAccessException, IOException {

        log.infof("Resolve jwell-mvc default configuration [%s] for [%s]", CONFIG_FILE_PATH,
                DEFAULT_RESOLVE_CLASS.getName());
        // 加载默认配置文件
        doResolve(DEFAULT_RESOLVE_CLASS, CONFIG_FILE_PATH);

        log.infof("Resolve custom configuration [%s] for [%s]", fileName,
                PROVIDER_RESOLVE_CLASS.getName());
        // 加载用户配置文件
        doResolve(PROVIDER_RESOLVE_CLASS, fileName);

    }

    private static final void doResolve(Class<? extends Resolve> resolveClass, String fileName)
            throws InstantiationException, IllegalAccessException, IOException {
        if (Strings.isBlank(fileName)) {
            return;
        }
        Resolve resolve = resolveClass.newInstance();
        String file = readConfigFile(fileName);
        resolve.resolve(file, Mvcs.getInstance());
    }

    private static final String readConfigFile(String fileName) throws IOException {

        InputStream in = Bootstrap.class.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer file = new StringBuffer(64);
        String line = null;
        while ((line = reader.readLine()) != null) {
            file.append(line).append("\r\n");
        }
        return file.toString();
    }

    @SuppressWarnings("unchecked")
    private static final void init() throws ServletException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Config cfg = Mvcs.get(Mvcs.CONFIG_LOADER);
        String loader = cfg.getString(Loader.class.getName());
        if (Strings.isBlank(loader)) {
            throw new ServletException("Class loader is empty , please setting loader class !");
        }
        log.info("Initial jwell-mvc class loader ! ");
        Class<Loader> loaderClass = (Class<Loader>) Class.forName(loader);
        LOADER = loaderClass.newInstance();
        log.infof("Jwell class loader is [%s]", LOADER.getClass().getName());
    }

}
