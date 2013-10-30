package com.fenwell.jwell.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.mvc.api.ActionDriver;
import com.fenwell.jwell.mvc.api.Logger;
import com.fenwell.jwell.mvc.api.ScanHandler;
import com.fenwell.jwell.utils.Strings;

public class FilterDispatcher implements Filter {

    private static final Logger log = Logs.getLogger();

    private static String encode = "UTF-8";

    private ActionDriver actionDriver;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        setHandler(request, response);
        try {
            String suffix = Mvcs.get(Mvcs.CONFIG_MVC).getString("suffix");
            actionDriver.dispatcher(request, response, chain, suffix);
        } catch (Exception e) {
            log.error(e);
            new ServletException(e);
        }
    }

    public void init(FilterConfig conf) throws ServletException {
        String fileName = conf.getInitParameter("config");
        String resolveClass = conf.getInitParameter("resolve");

        log.info("Jwell-mvc starting... ");
        Bootstrap.startup(fileName, resolveClass);
        Mvcs.setFilterConfig(conf);

        ScanHandler classScan = Bootstrap.load(ScanHandler.class);
        actionDriver = Bootstrap.load(ActionDriver.class);

        List<String> pack = Mvcs.get(Mvcs.CONFIG_MVC).getList("package", String.class);
        actionDriver.scan(pack, classScan);
        
        String ec = Mvcs.get(Mvcs.CONFIG_MVC).getString("encode");
        encode = Strings.defVal(ec, encode);
        
        // 不管好用不好用，GC一下吧。
        System.gc();
    }

    private void setHandler(HttpServletRequest req, HttpServletResponse resp)
            throws UnsupportedEncodingException {
        req.setCharacterEncoding(encode);
        resp.setCharacterEncoding(encode);
    }

}
