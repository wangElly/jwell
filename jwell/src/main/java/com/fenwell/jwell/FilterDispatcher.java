package com.fenwell.jwell;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.api.ActionHandler;

public class FilterDispatcher implements Filter {

    private static final Log log = LogFactory.getLog(FilterDispatcher.class);

    private ActionHandler actionHandler;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            actionHandler.dispatcher(req, resp, chain);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public void init(FilterConfig conf) throws ServletException {
        log.info("Jwell starting...");
        Bootstrap.startup(conf);
        actionHandler = Mvcs.getBean("actionHandler");
        actionHandler.init();
    }

}
