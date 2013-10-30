package com.fenwell.jwell.utils;

public class Javassists {

    /**
     * 将基本数据类型转换成包装类型
     * 
     * @param type
     * @param index
     * @return
     */
    public static String primitive2Wrap(Class<?> type, int index) {
        if (type.equals(int.class)) {
            return "new Integer($" + index + ")";
        } else if (type.equals(long.class)) {
            return "new Long($" + index + ")";
        } else if (type.equals(short.class)) {
            return "new Short($" + index + ")";
        } else if (type.equals(byte.class)) {
            return "new Byte($" + index + ")";
        } else if (type.equals(float.class)) {
            return "new Float($" + index + ")";
        } else if (type.equals(double.class)) {
            return "new Double($" + index + ")";
        } else if (type.equals(char.class)) {
            return "new Character($" + index + ")";
        } else if (type.equals(char.class)) {
            return "new Boolean($" + index + ")";
        } else {
            return "null";
        }
    }

}
