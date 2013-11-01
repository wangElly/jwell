package com.fenwell.jwell.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewHandler {

    public void execute(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception;

}
