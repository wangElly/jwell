package com.fenwell.util;

import java.util.Collection;

public class Collections {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

}
