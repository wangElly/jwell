package com.fenwell.jwell.spi.param;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.annotation.Param;
import com.fenwell.jwell.api.ParamHandler;
import com.fenwell.jwell.api.UploadHandler;
import com.fenwell.jwell.spi.pojo.FileMeta;
import com.fenwell.util.Arrays;
import com.fenwell.util.Collections;
import com.fenwell.util.Convert;
import com.fenwell.util.Reflects;
import com.fenwell.util.Strings;

public class DefaultParamHandler implements ParamHandler {

    private static final Log log = LogFactory.getLog(DefaultParamHandler.class);

    private UploadHandler uploadHandler;

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
        // 上传文件~！
        if (eq(t, File.class, FileMeta.class)) {
            return isFileType(request, name);
        }
        // 支持数组类型
        if (t.isArray()) {
            return isArray(t, request, name);
        }
        Object rst = null;
        if ((rst = isPrimitive(t, val, defVal)) != null) {
            return rst;
        }
        return rst;
    }

    private Object isArray(Class<?> t, HttpServletRequest request, String name) {
        if (!arrayIsPrimitive(t)) {
            String msg = "Currently not supports [%s] type , the current support type has : String , int , short , long , double , float , boolean , byte , char , File , FileMeta";
            log.warn(String.format(msg, t));
            return null;
        }
        String[] array = request.getParameterValues(name);
        if (Arrays.isEmpty(array)) {
            return null;
        }
        if (eq(t, String[].class)) {
            return array;
        }
        if (eq(t, int[].class, Integer[].class)) {
            return convertIntArray(array);
        }
        if (eq(t, short[].class, Short[].class)) {
            return convertShortArray(array);
        }
        if (eq(t, long[].class, Long[].class)) {
            return convertLongArray(array);
        }
        if (eq(t, double[].class, Double[].class)) {
            return convertDoubleArray(array);
        }
        if (eq(t, float[].class, Float[].class)) {
            return convertFloatArray(array);
        }
        if (eq(t, boolean[].class, Boolean[].class)) {
            return convertBooleanArray(array);
        }
        if (eq(t, byte[].class, Byte[].class)) {
            return convertByteArray(array);
        }
        if (eq(t, char[].class, Character[].class)) {
            return convertCharArray(array);
        }
        if (eq(t, File[].class, FileMeta[].class)) {
            List<File> files = uploadHandler.upload(request, name);
            return files.toArray();
        }
        return null;
    }

    private int[] convertIntArray(String[] array) {
        int[] temp = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Int(array[i], 0);
        }
        return temp;
    }

    private short[] convertShortArray(String[] array) {
        short[] temp = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Short(array[i], (short) 0);
        }
        return temp;
    }

    private long[] convertLongArray(String[] array) {
        long[] temp = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Long(array[i], 0);
        }
        return temp;
    }

    private double[] convertDoubleArray(String[] array) {
        double[] temp = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Double(array[i], 0.0);
        }
        return temp;
    }

    private float[] convertFloatArray(String[] array) {
        float[] temp = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Float(array[i], 0.0f);
        }
        return temp;
    }

    private boolean[] convertBooleanArray(String[] array) {
        boolean[] temp = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Boolean(array[i], false);
        }
        return temp;
    }

    private byte[] convertByteArray(String[] array) {
        byte[] temp = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Byte(array[i], (byte) 0);
        }
        return temp;
    }

    private char[] convertCharArray(String[] array) {
        char[] temp = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            temp[i] = Convert.string2Char(array[i], (char) 0);
        }
        return temp;
    }

    /**
     * 目前支持的数组类型
     * 
     * 有：String , int , short , long , double , float , boolean , byte , char ,
     * 
     * File , FileMeta
     * 
     * @param t
     * @return
     */
    private boolean arrayIsPrimitive(Class<?> t) {
        return eq(t, String[].class, int[].class, Integer[].class, short[].class, Short[].class,
                long[].class, Long[].class, double[].class, Double[].class, float[].class,
                Float[].class, boolean[].class, Boolean[].class, byte[].class, Byte[].class,
                char[].class, Character[].class, File[].class, FileMeta[].class);
    }

    private File isFileType(HttpServletRequest request, String name) {
        List<File> files = uploadHandler.upload(request, name);
        if (!Collections.isEmpty(files)) {
            return files.get(0);
        }
        return null;
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

    public void setUploadHandler(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

}
