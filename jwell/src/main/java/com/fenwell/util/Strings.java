package com.fenwell.util;


public class Strings  {
    
    public static final String EMPTY = "";

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String defVal(String val, String def) {
        if (isEmpty(val)) {
            return def;
        }
        return val;
    }
}
