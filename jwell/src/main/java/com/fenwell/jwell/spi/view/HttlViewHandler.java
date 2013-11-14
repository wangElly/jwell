package com.fenwell.jwell.spi.view;

import httl.Template;
import httl.web.WebEngine;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.api.ViewHandler;

public class HttlViewHandler implements ViewHandler {

    protected String encode = "UTF-8";

    public void execute(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {

        if (obj == null) {
            return;
        }

        response.setCharacterEncoding(encode);
        response.setContentType("text/html;charset=" + encode);

        String page = obj.toString();
        WebEngine.setRequestAndResponse(request, response);
        Template template = WebEngine.getEngine().getTemplate(page);

        ServletOutputStream out = response.getOutputStream();
        template.render(out);
        
        out.flush();
        out.close();

    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

}
