package com.fenwell.jwell.mvc.spi.view;

import httl.Template;
import httl.web.WebEngine;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.ViewHandler;
import com.fenwell.jwell.utils.Strings;

public class HttlViewHandler implements ViewHandler {

    private static String encode = "UTF-8";

    public HttlViewHandler() {
        String ec = Mvcs.get(Mvcs.CONFIG_MVC).getString("encode");
        encode = Strings.defVal(ec, encode);
    }

    public void execute(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {

        response.setCharacterEncoding(encode);
        response.setContentType("text/html;charset=" + encode);

        String page = obj.toString();
        WebEngine.setRequestAndResponse(request, response);
        Template template = WebEngine.getEngine().getTemplate(page);

        Map<String, Object> parameter = null;
        setRequestParemeter(request, parameter);
        setSessionParemeter(request.getSession(), parameter);

        ServletOutputStream out = response.getOutputStream();
        template.render(parameter, out);
        out.flush();
        out.close();

    }

    protected String getTemplatePath(String page) {
        String path = Mvcs.servletContext().getRealPath("/");
        page = page.replace("/", File.separator).replace("\\", File.separator);
        return path + page;
    }

    private void setSessionParemeter(HttpSession session, Map<String, Object> map) {
        Enumeration<?> enums = session.getAttributeNames();
        if (enums.hasMoreElements()) {
            map = newMap(map);
            while (enums.hasMoreElements()) {
                String key = enums.nextElement().toString();
                Object value = session.getAttribute(key);
                map.put(key, value);
            }
        }
    }

    protected void setRequestParemeter(HttpServletRequest req, Map<String, Object> map) {
        Enumeration<?> enums = req.getAttributeNames();
        if (enums.hasMoreElements()) {
            map = newMap(map);
            while (enums.hasMoreElements()) {
                String key = enums.nextElement().toString();
                Object value = req.getAttribute(key);
                map.put(key, value);
            }
        }
    }

    protected Map<String, Object> newMap(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return map;
    }

}
