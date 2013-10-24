package com.fenwell.jwell.mvc.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.mvc.Bootstrap;
import com.fenwell.jwell.mvc.Logs;
import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.annotation.Action;
import com.fenwell.jwell.mvc.api.annotation.Interceptor;
import com.fenwell.jwell.mvc.api.annotation.Param;
import com.fenwell.jwell.mvc.api.enums.MethodType;
import com.fenwell.jwell.mvc.api.model.ActionContent;
import com.fenwell.jwell.utils.Arrays;
import com.fenwell.jwell.utils.Collections;
import com.fenwell.jwell.utils.Strings;

public abstract class ActionDriver {

    private static final Logger log = Logs.getLogger();

    private static final Map<String, ActionContent> actions = new HashMap<String, ActionContent>();

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private ViewHandler viewHandler;

    private ParamHandler paramHandler;

    /**
     * 页面不存在
     */
    private static final int PAGE_404 = 404;
    
    /**
     * 方法不被允许 
     */
    private static final int PAGE_405 = 405;

    public ActionDriver() {
        this.viewHandler = Bootstrap.load(ViewHandler.class);
        this.paramHandler = Bootstrap.load(ParamHandler.class);
    }

    @SuppressWarnings("deprecation")
    public void dispatcher(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, String suffix) throws Exception {
        String uri = request.getRequestURI();
        if (!uri.endsWith(suffix)) {
            chain.doFilter(request, response);
            return;
        }
        ActionContent act = getAction(uri);
        if (act == null) {
            String msg = "Unable to find the page [" + uri + "]";
            response.setStatus(PAGE_404, msg);
            return;
        }
        MethodType mt = act.getMethodType();
        if (mt != MethodType.ALL) {
            String mtd = request.getMethod();
            MethodType reqMT = mtd.equals(METHOD_GET) ? MethodType.GET
                    : mtd.equals(METHOD_POST) ? MethodType.POST : null;
            if (mt != reqMT) {
                String msg = "HTTP method GET is not supported by this URL";
                response.setStatus(PAGE_405, msg);
                return ;
            }
        }
        Object interResult = doInterceptor(request, response, act.getInterceptors());
        if (interResult != null) {
            viewHandler.execute(request, response, interResult);
            return;
        }
        Object[] param = createParam(request, response, act);
        Object actionResult = doAction(request, response, act, param);
        viewHandler.execute(request, response, actionResult);
        param = null;
        actionResult = null;
    }

    private Object[] createParam(HttpServletRequest request, HttpServletResponse response,
            ActionContent act) {
        Class<?>[] paramClass = act.getParamClass();
        if (paramClass == null) {
            return null;
        }
        Param[] paramAnns = act.getParamAnnotation();
        int length = paramClass.length;
        Object[] param = new Object[length];
        for (int i = 0; i < length; i++) {
            param[i] = setParameter(request, response, paramClass[i], paramAnns[i]);
        }
        return param;
    }

    private ActionContent getAction(String uri) {
        if (Strings.isBlank(uri)) {
            return null;
        }
        String basePath = Mvcs.servletContext().getContextPath();
        String suffix = Mvcs.get(Mvcs.CONFIG_MVC).getString("suffix");
        uri = uri.replace(basePath, Strings.EMPTY).replace(suffix, Strings.EMPTY);
        ActionContent ac = actions.get(uri);
        return ac;
    }

    private Object setParameter(HttpServletRequest request, HttpServletResponse response,
            Class<?> cls, Param param) {
        return paramHandler.execute(request, response, cls, param);
    }

    public void scan(List<String> pack, ScanHandler scan) {
        if (Collections.isEmpty(pack)) {
            return;
        }
        List<Class<?>> allCls = scan.scan(pack);
        resolveAction(allCls);
        // 扫描完毕，清空加载类集合
        if (!Collections.isEmpty(allCls)) {
            allCls.clear();
            allCls = null;
        }
    }

    private void resolveAction(List<Class<?>> allCls) {
        if (Collections.isEmpty(allCls)) {
            return;
        }
        for (Class<?> cls : allCls) {
            resolveAction(cls);
        }
    }

    private void resolveAction(Class<?> cls) {
        Action act = cls.getAnnotation(Action.class);
        Method[] methods = cls.getDeclaredMethods();
        if (Arrays.isEmpty(methods)) {
            return;
        }
        for (Method mtd : methods) {
            Action at = mtd.getAnnotation(Action.class);
            if (at != null) {
                ActionContent ac = createActionContent(act, at, mtd, cls);
                if (ac != null) {
                    String suffix = Mvcs.get(Mvcs.CONFIG_MVC).getString("suffix");
                    String mtdName = ac.getMethod().toString();
                    String mtdType = ac.getMethodType().toString();
                    log.infof("Scan to action -> %s%s & method -> %s & method type -> %s",
                            ac.getUri(), suffix, mtdName, mtdType);
                    actions.put(ac.getUri(), ac);
                }
            }
        }
    }

    private ActionContent createActionContent(Action parent, Action act, Method mtd, Class<?> cls) {
        String split = Strings.defVal(Mvcs.get(Mvcs.CONFIG_MVC).getString("split"), "/");
        StringBuffer uri = new StringBuffer(32);
        if (parent != null) {
            uri.append(split).append(parent.value());
        }
        uri.append(split).append(act.value());
        MethodType mtdType = act.methodType();
        Class<com.fenwell.jwell.mvc.api.Interceptor>[] interceptors = null;
        Interceptor inters = mtd.getAnnotation(Interceptor.class);
        if (inters != null) {
            interceptors = inters.value();
        }
        Class<?>[] paramClass = createParamClass(mtd);
        Param[] paramAnns = createParamAnnotation(mtd);
        ActionContent ac = new ActionContent();
        ac.setUri(uri.toString());
        ac.setMethod(mtd);
        ac.setClasz(cls);
        ac.setParamAnnotation(paramAnns);
        ac.setInterceptors(interceptors);
        ac.setMethodType(mtdType);
        ac.setParamClass(paramClass);
        return ac;
    }

    private Param[] createParamAnnotation(Method mtd) {
        if (mtd == null) {
            return null;
        }
        Annotation[][] ans = mtd.getParameterAnnotations();
        if (Arrays.isEmpty(ans)) {
            return null;
        }
        Param[] params = new Param[ans.length];
        for (int i = 0; i < ans.length; i++) {
            Annotation[] an = ans[i];
            if (an.length == 0) {
                params[i] = null;
                continue;
            } else {
                for (Annotation a : an) {
                    if (a instanceof Param) {
                        params[i] = (Param) a;
                        break;
                    }
                }
            }
        }
        return params;
    }

    private Class<?>[] createParamClass(Method mtd) {
        if (mtd == null) {
            return null;
        }
        Class<?>[] clss = mtd.getParameterTypes();
        if (Arrays.isEmpty(clss)) {
            return null;
        }
        return clss;
    }

    public abstract Object doAction(HttpServletRequest request, HttpServletResponse response,
            ActionContent action, Object[] param) throws Exception;

    public abstract Object doInterceptor(HttpServletRequest request, HttpServletResponse response,
            Class<com.fenwell.jwell.mvc.api.Interceptor>[] interceptors)
            throws InstantiationException, IllegalAccessException;

}
