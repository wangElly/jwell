package com.fenwell.jwell.spi.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fenwell.jwell.Mvcs;
import com.fenwell.jwell.annotation.Action;
import com.fenwell.jwell.annotation.Param;
import com.fenwell.jwell.api.ActionHandler;
import com.fenwell.jwell.api.Interceptors;
import com.fenwell.jwell.api.ParamHandler;
import com.fenwell.jwell.api.ViewHandler;
import com.fenwell.jwell.enums.MethodType;
import com.fenwell.jwell.spi.pojo.ActionContent;
import com.fenwell.util.Arrays;
import com.fenwell.util.Maps;
import com.fenwell.util.Strings;

/**
 * 代码太过冗余和复杂，准备重构
 * 
 * @author Wang12
 * 
 */
public class MapperActionHandler implements ActionHandler {

    private static final Log log = LogFactory.getLog(MapperActionHandler.class);

    /**
     * 404 页面不存在
     */
    private static final int PAGE_404 = 404;

    /**
     * 405 方法不被允许
     */
    private static final int PAGE_405 = 405;

    /**
     * HTTP GET 提交方式
     */
    private static final String METHOD_GET = "GET";

    /**
     * HTTP POST 提交方式
     */
    private static final String METHOD_POST = "POST";

    /**
     * URL分隔符
     */
    private String splitChar = "/";

    /**
     * 拦截后缀名
     */
    private String urlSuffix = ".do";

    /**
     * 参数处理器
     */
    private ParamHandler paramHandler;

    /**
     * 视图处理器
     */
    private ViewHandler viewHandler;

    /**
     * Action Continer !
     */
    private Map<String, ActionContent> actions = new HashMap<String, ActionContent>();

    @SuppressWarnings("deprecation")
    public void dispatcher(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws Exception {
        // 获取action的URI
        String actUri = getActinURI(request);
        // 是否是Action请求，如果不是，则继续执行
        if (actUri == null) {
            chain.doFilter(request, response);
            return;
        }
        ActionContent actionContent = actions.get(actUri);
        // ActionContent 不存在 ， 抛出404
        if (actionContent == null) {
            String msg = "Unable to find the page [" + actUri + "]";
            response.setStatus(PAGE_404, msg);
            return;
        }
        // 检查请求方式是否正确
        if (validMethodType(actionContent, request)) {
            String type = request.getMethod();
            String msg = "HTTP method " + type + " is not supported by this URL";
            response.setStatus(PAGE_405, msg);
            return;
        }
        // 执行拦截器和Action
        invoke(request, response, actionContent);
    }

    /**
     * 执行拦截器和Action
     * 
     * @param request
     * @param response
     * @param actionContent
     * @throws Exception
     */
    private void invoke(HttpServletRequest request, HttpServletResponse response,
            ActionContent actionContent) throws Exception {
        Object interResult = doInterceptor(request, response, actionContent.getInterceptors());
        // 如果被拦截器拦截，则执行视图
        if (interResult != null) {
            viewHandler.execute(request, response, interResult);
            return;
        }
        // 组织方法参数
        Object[] param = createParam(request, response, actionContent);
        // 执行Action
        Object actionResult = doAction(request, response, actionContent, param);
        viewHandler.execute(request, response, actionResult);
        param = null;
        actionResult = null;
    }

    /**
     * 执行Action
     * 
     * @param request
     * @param response
     * @param actionContent
     * @param param
     * @return
     */
    private Object doAction(HttpServletRequest request, HttpServletResponse response,
            ActionContent actionContent, Object[] param) {
        Object target = actionContent.getInstance();
        int methodIndex = actionContent.getMethodIndex();
        MethodAccess method = actionContent.getMethod();
        return method.invoke(target, methodIndex, param);
    }

    /**
     * 创建参数
     * 
     * @param request
     * @param response
     * @param act
     * @return
     */
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

    /**
     * 设置参数
     * 
     * @param request
     * @param response
     * @param cls
     * @param param
     * @return
     */
    private Object setParameter(HttpServletRequest request, HttpServletResponse response,
            Class<?> cls, Param param) {
        return paramHandler.execute(request, response, cls, param);
    }

    /**
     * 执行拦截器
     * 
     * @param request
     * @param response
     * @param interceptors
     * @return
     */
    private Object doInterceptor(HttpServletRequest request, HttpServletResponse response,
            Class<? extends Interceptors>[] interceptors) {
        if (Arrays.isEmpty(interceptors)) {
            return null;
        }
        Object result = null;
        for (Class<? extends Interceptors> cls : interceptors) {
            Interceptors interceptor = Mvcs.getBean(cls);
            result = interceptor.server(request, response);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    /**
     * 验证请求类型 POST/GET/ALL
     * 
     * @param actionContent
     * @param request
     * @return
     */
    private boolean validMethodType(ActionContent actionContent, HttpServletRequest request) {
        MethodType type = actionContent.getMethodType();
        if (type == MethodType.ALL) {
            return false;
        }
        String mtd = request.getMethod();
        MethodType reqMT = mtd.equals(METHOD_GET) ? MethodType.GET
                : mtd.equals(METHOD_POST) ? MethodType.POST : null;
        if (type != reqMT) {
            return true;
        }
        return false;
    }

    /**
     * 获取到Action 的URI
     * 
     * @param request
     * @return
     */
    private String getActinURI(HttpServletRequest request) {
        String basePath = request.getContextPath();
        String uri = request.getRequestURI().replace(basePath, Strings.EMPTY).toLowerCase();
        if (!uri.endsWith(urlSuffix)) {
            return null;
        }
        uri = uri.replace(urlSuffix, Strings.EMPTY).substring(1);
        return uri;
    }

    /**
     * 初始化
     */
    public void init() throws ServletException {
        viewHandler = Mvcs.getBean("viewHandler");
        paramHandler = Mvcs.getBean("paramHandler");
        ApplicationContext ctx = Mvcs.getApplicationContext();
        Map<String, Object> map = ctx.getBeansWithAnnotation(Action.class);
        if (Maps.isEmpty(map)) {
            return;
        }
        for (Object obj : map.values()) {
            makeAction(obj);
        }
    }

    private void makeAction(Object obj) {
        Class<?> cls = obj.getClass();
        Method[] mtds = cls.getDeclaredMethods();
        if (Arrays.isEmpty(mtds)) {
            return;
        }
        Action parent = cls.getAnnotation(Action.class);
        String parentURI = parent.path();
        for (Method mtd : mtds) {
            Action child = mtd.getAnnotation(Action.class);
            if (child == null) {
                continue;
            }
            String childURI = Strings.defVal(child.path(), mtd.getName());
            String uri = makeURI(parentURI, childURI);
            ActionContent ac = makeActionContent(parent, child, mtd, obj);
            actions.put(uri, ac);
            String msg = String.format("Scan action -> %s%s method type is %s invoke method in %s",
                    uri, urlSuffix, child.type(), mtd);
            log.info(msg);
        }
    }

    private ActionContent makeActionContent(Action parent, Action child, Method mtd, Object obj) {
        Class<?>[] paramClass = createParamClass(mtd);
        Param[] paramAnns = createParamAnnotation(mtd);
        MethodType type = child.type();
        MethodAccess method = MethodAccess.get(obj.getClass());
        int methodIndex = method.getIndex(mtd.getName(), mtd.getParameterTypes());
        Class<? extends Interceptors>[] interceptors = createInterceptors(parent, child);

        ActionContent ac = new ActionContent();
        ac.setParamClass(paramClass);
        ac.setParamAnnotation(paramAnns);
        ac.setMethodType(type);
        ac.setInstance(obj);
        ac.setMethod(method);
        ac.setMethodIndex(methodIndex);
        ac.setInterceptors(interceptors);
        return ac;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Interceptors>[] createInterceptors(Action parent, Action child) {
        List<Class<? extends Interceptors>> list = new ArrayList<Class<? extends Interceptors>>();
        if (child.useParentInterceptor()) {
            if (!Arrays.isEmpty(parent.interceptors())) {
                for (Class<? extends Interceptors> i : parent.interceptors()) {
                    list.add(i);
                }
            }
        }
        if (!Arrays.isEmpty(child.interceptors())) {
            for (Class<? extends Interceptors> i : child.interceptors()) {
                list.add(i);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        Class<? extends Interceptors>[] arrays = new Class[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = list.get(i);
        }
        return arrays;
    }

    private String makeURI(String parentURI, String childURI) {
        StringBuffer uri = new StringBuffer(32);
        if (!Strings.isBlank(parentURI)) {
            uri.append(parentURI).append(splitChar);
        }
        uri.append(childURI);
        return uri.toString();
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

    public void setSplitChar(String splitChar) {
        this.splitChar = splitChar;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

}
