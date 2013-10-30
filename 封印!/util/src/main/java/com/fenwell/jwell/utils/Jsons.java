package com.fenwell.jwell.utils;

public class Jsons {

    public static String removeComment(String json) {
        if (Strings.isBlank(json)) {
            return json;
        }
        String single = "//.*";
        String more = "/\\*[\\S\\s]*?\\*/";
        return json.replaceAll(single, Strings.EMPTY).replaceAll(more, Strings.EMPTY);
    }

}
