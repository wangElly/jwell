package com.fenwell.jwell.mybatis.operation;

import java.lang.reflect.Method;

import com.fenwell.util.Asserts;
import com.fenwell.util.Convert;

public abstract class AbstractOperation {

    protected boolean returnTypeIsVoid(Method mtd) {
        Class<?> cls = mtd.getReturnType();
        return Asserts.classIsVoid(cls);
    }

    protected boolean returnTypeIsPrimitive(Method mtd) {
        Class<?> cls = mtd.getReturnType();
        return Asserts.classIsPrimitive(cls);
    }

    protected Object returnPrimitiveType(Class<?> cls, int result) {
        Object value = null;
        if (Asserts.classIsBoolean(cls)) {
            value = result > 0;
        }
        if (Asserts.classIsInt(cls)) {
            value = result;
        }
        if (Asserts.classIsLong(cls)) {
            value = Convert.string2Long(result + "", 0);
        }
        if (Asserts.classIsShort(cls)) {
            value = Convert.string2Short(result + "", (short) 0);
        }
        if (Asserts.classIsDouble(cls)) {
            value = Convert.string2Double(result + "", 0.0);
        }
        if (Asserts.classIsFloat(cls)) {
            value = Convert.string2Float(result + "", 0.0f);
        }
        if (Asserts.classIsChar(cls)) {
            value = Convert.string2Char(result + "", (char) 0);
        }
        if (Asserts.classIsByte(cls)) {
            value = Convert.string2Byte(result + "", (byte) 0);
        }
        return value;
    }

    public abstract Object execute(String id, Method mtd, Object param);

}
