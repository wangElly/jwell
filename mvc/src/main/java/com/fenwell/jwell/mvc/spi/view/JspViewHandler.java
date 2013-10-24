package com.fenwell.jwell.mvc.spi.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.mvc.api.ViewHandler;

public class JspViewHandler implements ViewHandler {

    public void execute(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {
        if (obj == null) {
            return;
        }
        if (obj instanceof String) {
            String path = obj.toString();
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

}
