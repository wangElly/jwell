package com.fenwell.util;

public class Convert {
    
    /**
     * String 转换成 int
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static int string2Int(String str, int def) {
        if (str == null) {
            return def;
        }
        int val = -1;
        try {
            val = Integer.parseInt(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * 
     * String 转换成 byte
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static byte string2Byte(String str, byte def) {
        if (str == null) {
            return def;
        }
        byte val = -1;
        try {
            val = Byte.parseByte(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;

    }

    /**
     * String 转换成 short
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static short string2Short(String str, short def) {
        if (str == null) {
            return def;
        }
        short val = -1;
        try {
            val = Short.parseShort(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * String 转换成 long
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static long string2Long(String str, long def) {
        if (str == null) {
            return def;
        }
        long val = -1;
        try {
            val = Long.parseLong(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * String 转换成 float
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static float string2Float(String str, float def) {
        if (str == null) {
            return def;
        }
        float val = -1;
        try {
            val = Float.parseFloat(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * String 转换成 double
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static double string2Double(String str, double def) {
        if (str == null) {
            return def;
        }
        double val = -1;
        try {
            val = Double.parseDouble(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * String 转换成 char
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static char string2Char(String str, char def) {
        if (str == null) {
            return def;
        }
        char val = 0;
        try {
            val = str.charAt(0);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

    /**
     * String 转换成 boolean
     * 
     * @param str 字符串
     * @param def 如果字符串错误，则使用默认值
     * @return
     */
    public static boolean string2Boolean(String str, boolean def) {
        if (str == null) {
            return def;
        }
        boolean val = false;
        try {
            val = Boolean.parseBoolean(str);
        } catch (Throwable e) {
            val = def;
        }
        return val;
    }

}
