package com.fenwell.util;


public class Arrays {

    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static String toString(Object[] objs) {
        return java.util.Arrays.toString(objs);
    }

    public static <T> T[] dynamicArray(T... elem) {
        return elem;
    }

}
