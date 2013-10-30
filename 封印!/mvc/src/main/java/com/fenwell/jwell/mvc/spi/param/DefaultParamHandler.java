package com.fenwell.jwell.mvc.spi.param;

import java.lang.reflect.Field;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.ParamHandler;
import com.fenwell.jwell.mvc.api.annotation.Param;
import com.fenwell.jwell.utils.Convert;
import com.fenwell.jwell.utils.Reflects;
import com.fenwell.jwell.utils.Strings;

public class DefaultParamHandler implements ParamHandler {

    private static String encode = "UTF-8";

    public DefaultParamHandler() {
        String ec = Mvcs.get(Mvcs.CONFIG_MVC).getString("encode");
        encode = Strings.defVal(ec, encode);
    }

    public Object execute(HttpServletRequest request, HttpServletResponse response, Class<?> t,
            Param param) {
        Object rst = null;
        if ((rst = isHttpInnerObject(request, response, t)) != null) {
            return rst;
        }
        if (param == null || Strings.isEmpty(param.value())) {
            return null;
        }
        String name = param.value();
        String defVal = param.defVal();
        String val = request.getParameter(name);
        rst = singleValue(t, name, val, defVal, request);
        if (rst == null) {
            rst = isObject(request, t, name);
        }
        return rst;
    }

    private Object singleValue(Class<?> t, String name, String val, String defVal,
            HttpServletRequest request) {
        if (eq(t, String.class)) {
            return Strings.defVal(val, defVal);
        }
        Object rst = null;
        if ((rst = isPrimitive(t, val, defVal)) != null) {
            return rst;
        }
        return rst;
    }

    private Object isObject(HttpServletRequest request, Class<?> t, String name) {
        Field[] fields = t.getDeclaredFields();
        Object o = null;
        for (Field field : fields) {
            String fName = name + "." + field.getName();
            String val = request.getParameter(fName);
            if (val == null) {
                continue;
            }
            Object obj = singleValue(field.getType(), fName, val, null, request);
            try {
                o = setField(o, obj, t, field.getName());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return o;
    }

    private Object setField(Object o, Object val, Class<?> cls, String fName) throws Exception {
        if (o == null) {
            o = Reflects.createBean(cls);
        }
        Reflects.setter(o, val, fName);
        return o;
    }

    /**
     * 服务器内置对象
     * 
     * request , response , session
     * 
     * @param request
     * @param response
     * @param t
     * @return
     */
    private Object isHttpInnerObject(HttpServletRequest request, HttpServletResponse response,
            Class<?> t) {
        Object obj = null;
        if (eq(t, ServletRequest.class) || eq(t, HttpServletRequest.class)) {
            obj = request;
        } else if (eq(t, ServletResponse.class) || eq(t, HttpServletResponse.class)) {
            obj = response;
        } else if (eq(t, HttpSession.class)) {
            obj = request.getSession();
        }
        return obj;
    }

    /**
     * 基本数据类型
     * 
     * byte,shot,int,long,float,double,char,boolean
     * 
     * @param request
     * @param t
     * @param name
     * @param defVal
     * @return
     */
    private Object isPrimitive(Class<?> t, String val, String defVal) {
        Object obj = null;
        String v = Strings.defVal(val, defVal);
        if (eq(t, int.class, Integer.class)) {
            obj = Convert.string2Int(v, 0);
        } else if (eq(t, long.class, Long.class)) {
            obj = Convert.string2Long(v, 0l);
        } else if (eq(t, short.class, Short.class)) {
            obj = Convert.string2Short(v, (short) 0);
        } else if (eq(t, float.class, Float.class)) {
            obj = Convert.string2Float(v, 0.0f);
        } else if (eq(t, double.class, Double.class)) {
            obj = Convert.string2Double(v, 0.0);
        } else if (eq(t, boolean.class, Boolean.class)) {
            obj = Convert.string2Boolean(v, false);
        } else if (eq(t, char.class, Character.class)) {
            obj = Convert.string2Char(v, (char) 0);
        } else if (eq(t, byte.class, Byte.class)) {
            obj = Convert.string2Byte(v, (byte) 0);
        }
        return obj;
    }

    private boolean eq(Object o1, Object o2) {
        return o1.equals(o2);
    }

    private boolean eq(Object o1, Object... o2) {
        for (Object o : o2) {
            if (o1.equals(o)) {
                return true;
            }
        }
        return false;
    }

}
