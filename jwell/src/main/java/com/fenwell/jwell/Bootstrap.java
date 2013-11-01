package com.fenwell.jwell;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fenwell.util.Strings;

public class Bootstrap {

    private static final Log log = LogFactory.getLog(Bootstrap.class);

    private Bootstrap() {
    }

    public static void startup(FilterConfig conf) throws ServletException {
        String fileName = conf.getInitParameter("config");
        if (Strings.isBlank(fileName)) {
            String exMsg = "Configuration file is empty , please set configuration in web.xml filter";
            throw new ServletException(exMsg);
        }
        log.info("Initial spring ApplicationContext ");
        ApplicationContext ctx = new ClassPathXmlApplicationContext(fileName);
        // 获取Mvcs的单例
        Mvcs mvcs = Mvcs.getInstance();
        // 设置applicationContext 和 filterConfig
        mvcs.setFilterConfig(conf);
        mvcs.setApplicationContext(ctx);
        // 设置后缀名
        String suffix = conf.getInitParameter("suffix");
        if (!Strings.isBlank(suffix)) {
            mvcs.setSuffix(suffix);
        }
    }

}
