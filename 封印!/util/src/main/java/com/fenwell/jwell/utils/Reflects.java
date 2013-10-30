package com.fenwell.jwell.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;

public class Reflects {

    /**
     * 缓存MethodAccess避免重复生成，增加系统开销
     */
    private static final Map<Class<?>, MethodAccess> METHOD_ACCESS_CACHED = new HashMap<Class<?>, MethodAccess>();

    /**
     * methodAccess 调用的次数
     */
    private static int METHOD_ACCESS_COUNT = 0;

    /**
     * methodAccess 调用的最大次数
     */
    private static int METHOD_ACCESS_MAX_COUNT = 10000;

    /**
     * methodAccess 达到最大次数的增加步长
     */
    private static int METHOD_ACCESS_COUNT_STEP = 1000;

    /**
     * 最后记录时间
     */
    private static long METHOD_ACCESS_LAST_RECORD_TIME = 0;

    /**
     * METHOD_ACCESS_MAX_COUNT 最小增加时间，默认2分钟
     * 
     * 如果 METHOD_ACCESS_MAX_COUNT 在 METHOD_ACCESS_MIN_TIME 时间内达到
     * METHOD_ACCESS_MAX_COUNT， 则 METHOD_ACCESS_MAX_COUNT 增加
     * METHOD_ACCESS_COUNT_STEP
     * 
     */
    private static long METHOD_ACCESS_MIN_TIME = 1000 * 60 * 2;

    /**
     * METHOD_ACCESS_MAX_COUNT 最小减少时间，默认1小时
     */
    private static long METHOD_ACCESS_MAX_TIME = 1000 * 60 * 60 * 1;

    public static <T> T createBean(Class<T> t) throws InstantiationException,
            IllegalAccessException {
        return t.newInstance();
    }

    public static String setName(String name) {
        return "set" + firstCharToUpper(name);
    }

    public static Object invoke(Object obj, String mtd, Object... param) {
        MethodAccess ma = getMethodAccess(obj.getClass());
        return ma.invoke(obj, mtd, param);
    }

    public static void setter(Object obj, Object val, String fileName) {
        if (obj == null || val == null || Strings.isEmpty(fileName)) {
            return;
        }
        MethodAccess ma = getMethodAccess(obj.getClass());
        String setName = setName(fileName);
        ma.invoke(obj, setName, val);
    }

    private static MethodAccess getMethodAccess(Class<?> cls) {
        MethodAccess ma = METHOD_ACCESS_CACHED.get(cls);
        if (ma == null) {
            ma = MethodAccess.get(cls);
            METHOD_ACCESS_CACHED.put(cls, ma);
            if (METHOD_ACCESS_CACHED.size() == 1) {
                METHOD_ACCESS_LAST_RECORD_TIME = System.currentTimeMillis();
            }
        }
        clearMethodAccessCache();
        return ma;
    }

    private static void clearMethodAccessCache() {
        if (METHOD_ACCESS_COUNT >= METHOD_ACCESS_MAX_COUNT) {
            long time = System.currentTimeMillis() - METHOD_ACCESS_LAST_RECORD_TIME;
            if (time <= METHOD_ACCESS_MIN_TIME) {
                METHOD_ACCESS_MAX_COUNT += METHOD_ACCESS_COUNT_STEP;
            } else if (time >= METHOD_ACCESS_MAX_TIME) {
                METHOD_ACCESS_MAX_COUNT -= METHOD_ACCESS_COUNT_STEP;
            }
        }
        METHOD_ACCESS_COUNT++;
    }

    private static String firstCharToUpper(String name) {
        if (Strings.isEmpty(name)) {
            return Strings.EMPTY;
        }
        if (name.length() < 2) {
            return name.toUpperCase();
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static <T> T makeClass(Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Method[] getMethods(Class<?> cls) {
        Method[] mtds = null;
        try {
            mtds = cls.getDeclaredMethods();
        } catch (Throwable e) {
            return new Method[0];
        }
        return mtds;
    }

    public static <T> T makeClass(Class<T> cls, Class<?>[] clsType, Object[] param) {
        T t = null;
        try {
            if (Arrays.isEmpty(param)) {
                t = cls.newInstance();
            } else {
                Constructor<? extends T> con = cls.getConstructor(clsType);
                t = con.newInstance(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String convertDefault(Class<?> cls) {
        if (cls.isPrimitive()) {
            if (cls.equals(int.class) || cls.equals(short.class) || cls.equals(byte.class)) {
                return "0";
            } else if (cls.equals(long.class)) {
                return "0l";
            } else if (cls.equals(float.class)) {
                return "0.0f";
            } else if (cls.equals(double.class)) {
                return "0.0d";
            } else if (cls.equals(char.class)) {
                return "0";
            } else if (cls.equals(boolean.class)) {
                return "false";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
