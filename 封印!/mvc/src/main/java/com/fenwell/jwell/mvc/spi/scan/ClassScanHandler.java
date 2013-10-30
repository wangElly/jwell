package com.fenwell.jwell.mvc.spi.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.fenwell.jwell.mvc.Logs;
import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.Logger;
import com.fenwell.jwell.mvc.api.ScanHandler;
import com.fenwell.jwell.utils.Collections;
import com.fenwell.jwell.utils.Strings;

public class ClassScanHandler implements ScanHandler {

    private static final Logger log = Logs.getLogger();

    public List<Class<?>> scan(List<String> packs) {

        if (Collections.isEmpty(packs)) {
            log.infof("Scan package is empty , please setting package !");
            return null;
        }

        log.infof("Scan package %s", packs.toString());

        String classPath = Mvcs.servletContext().getRealPath("/WEB-INF/classes/");
        Set<String> jars = Mvcs.servletContext().getResourcePaths("/WEB-INF/lib");

        List<Class<?>> allCls = new ArrayList<Class<?>>();
        List<Class<?>> tmpList = null;
        try {
            tmpList = scanClass(classPath);
        } catch (ClassNotFoundException e) {
            log.error(e);
        }
        if (!Collections.isEmpty(tmpList)) {
            allCls.addAll(tmpList);
            tmpList.clear();
            tmpList = null;
        }

        if (!Collections.isEmpty(jars)) {
            for (String path : jars) {
                tmpList = scanJar(path);
                if (!Collections.isEmpty(tmpList)) {
                    allCls.addAll(tmpList);
                    tmpList.clear();
                    tmpList = null;
                }
            }
        }
        return allCls;
    }

    private List<Class<?>> scanClass(String path) throws ClassNotFoundException {
        if (Strings.isEmpty(path)) {
            return null;
        }
        return doScan(path, path);
    }

    private List<Class<?>> scanJar(String path) {
        String jarFile = Mvcs.servletContext().getRealPath(path);
        List<Class<?>> clsList = null;
        try {
            JarFile file = new JarFile(jarFile);
            Enumeration<JarEntry> enu = file.entries();
            while (enu.hasMoreElements()) {
                JarEntry jar = enu.nextElement();
                String enuFile = jar.getName();
                if (enuFile.endsWith(".class")) {
                    String clsFile = convertToClassPath(enuFile, Strings.EMPTY);
                    Class<?> cls = loadCls(clsFile);
                    if (cls != null) {
                        if (clsList == null) {
                            clsList = new ArrayList<Class<?>>();
                        }
                        clsList.add(cls);
                    }
                }
            }
            file.close();
        } catch (IOException e) {
            log.error("Unable load jar file :" + jarFile, e);
        }
        return clsList;
    }

    private List<Class<?>> doScan(String filePath, String defPath) throws ClassNotFoundException {
        List<Class<?>> list = null;
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                List<Class<?>> tmpList = doScan(file.getAbsolutePath(), defPath);
                if (tmpList != null) {
                    if (list == null) {
                        list = new ArrayList<Class<?>>();
                    }
                    list.addAll(tmpList);
                    tmpList.clear();
                    tmpList = null;
                }
            } else {
                String path = file.getPath();
                if (path.endsWith(".class")) {
                    String clsPath = convertToClassPath(path, defPath);
                    Class<?> cls = loadCls(clsPath);
                    if (cls != null) {
                        if (list == null) {
                            list = new ArrayList<Class<?>>();
                        }
                        list.add(cls);
                    }
                }
            }
        }
        return list;
    }

    private Class<?> loadCls(String name) {
        Class<?> cls = null;
        try {
            cls = Class.forName(name);
        } catch (Throwable e) {
            if (log.isDebugEnabled())
                log.debugf("Can not load class [%s]", name);
        }
        return cls;
    }

    private String convertToClassPath(String path, String defPath) {
        if (!defPath.endsWith(File.separator)) {
            defPath += File.separator;
        }
        String clsPath = path.replace(defPath, Strings.EMPTY);
        clsPath = clsPath.substring(0, clsPath.lastIndexOf("."));
        clsPath = clsPath.replace("/", ".");
        clsPath = clsPath.replace("\\", ".");
        return clsPath;
    }

}
