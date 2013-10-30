package com.fenwell.jwell.mvc.spi.action;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.mvc.api.ActionDriver;
import com.fenwell.jwell.mvc.api.Interceptor;
import com.fenwell.jwell.mvc.api.model.ActionContent;
import com.fenwell.jwell.utils.Arrays;
import com.fenwell.jwell.utils.Reflects;

public class MapperActionDriver extends ActionDriver {

    @Override
    public Object doAction(HttpServletRequest request, HttpServletResponse response,
            ActionContent act, Object[] param) throws Exception {
        Method method = act.getMethod();
        String name = method.getName();
        Object result = Reflects.invoke(act.instance(), name, param);
        return result;
    }

    @Override
    public Object doInterceptor(HttpServletRequest request, HttpServletResponse response,
            Class<Interceptor>[] interceptors) throws InstantiationException,
            IllegalAccessException {
        // 如果没有拦截器，就赶紧回去吧！
        if (Arrays.isEmpty(interceptors)) {
            return null;
        }
        Object result = null;
        // 有拦截器的话，一层一层执行。 如果拦截器有返回，则停止！
        for (Class<Interceptor> inter : interceptors) {
            Interceptor i = Reflects.createBean(inter);
            result = i.server(request, response);
            i = null;
            if (result != null) {
                break;
            }
        }
        return result;
    }

}
