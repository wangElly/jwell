package com.fenwell.jwell.mvc.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {

    public Object server(HttpServletRequest request, HttpServletResponse response);

}
