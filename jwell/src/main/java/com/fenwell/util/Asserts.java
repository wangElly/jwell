package com.fenwell.util;

public class Asserts {

    protected static final String VOID = "void";

    /**
     * 判断class 是否为void
     * 
     * @param cls
     * @return
     */
    public static boolean classIsVoid(Class<?> cls) {
        return cls.equals(VOID);
    }

    /**
     * 判断Class 是否为基本类型，包括基本类型的包装类型。
     * 
     * @param cls
     * @return
     */
    public static boolean classIsPrimitive(Class<?> cls) {
        if (cls.isPrimitive()) {
            return true;
        }
        if (cls.equals(Integer.class) || cls.equals(Long.class) || cls.equals(Short.class)
                || cls.equals(Boolean.class) || cls.equals(Double.class) || cls.equals(Float.class)
                || cls.equals(Character.class) || cls.equals(Byte.class)) {
            return true;
        }
        return false;
    }

    /**
     * 判断class 是否为boolean 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsBoolean(Class<?> cls) {
        return cls.equals(boolean.class) || cls.equals(Boolean.class);
    }

    /**
     * 判断class 是否为int 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsInt(Class<?> cls) {
        return cls.equals(int.class) || cls.equals(Integer.class);
    }

    /**
     * 判断class 是否为long 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsLong(Class<?> cls) {
        return cls.equals(long.class) || cls.equals(Long.class);
    }

    /**
     * 判断class 是否为short 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsShort(Class<?> cls) {
        return cls.equals(short.class) || cls.equals(Short.class);
    }

    /**
     * 判断class 是否为double 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsDouble(Class<?> cls) {
        return cls.equals(double.class) || cls.equals(Double.class);
    }

    /**
     * 判断class 是否为float 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsFloat(Class<?> cls) {
        return cls.equals(float.class) || cls.equals(Float.class);
    }

    /**
     * 判断class 是否为char 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsChar(Class<?> cls) {
        return cls.equals(char.class) || cls.equals(Character.class);
    }

    /**
     * 判断class 是否为byte 类型，
     * 
     * @param cls
     * @return
     */
    public static boolean classIsByte(Class<?> cls) {
        return cls.equals(byte.class) || cls.equals(Byte.class);
    }

}
