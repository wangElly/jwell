package com.fenwell.jwell.utils;

public class Properties {

    private String file;

    private Properties(String file) {
        this.file = file;
    }

    public static Properties readProperties(String file) {
        return new Properties(file);
    }

    public String get(String key) {
        return null;
    }

    public String getDef(String key, String def) {
        return null;
    }

}
