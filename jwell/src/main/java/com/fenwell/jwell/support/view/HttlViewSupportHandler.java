package com.fenwell.jwell.support.view;

import java.util.Map;

import httl.Template;
import httl.web.WebEngine;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.spi.view.HttlViewHandler;

public class HttlViewSupportHandler extends HttlViewHandler {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {
        if (obj == null) {
            return;
        }

        if (obj instanceof View) {

            View view = (View) obj;
            String page = view.getPage();

            Map<String, Object> param = view.getParam();

            response.setCharacterEncoding(encode);
            response.setContentType("text/html;charset=" + encode);

            WebEngine.setRequestAndResponse(request, response);
            Template template = WebEngine.getEngine().getTemplate(page);

            ServletOutputStream out = response.getOutputStream();
            template.render(param, out);

            out.flush();
            out.close();
        } else {
            super.execute(request, response, obj);
        }
    }

}
