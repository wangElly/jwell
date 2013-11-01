package com.fenwell.jwell.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptors {
    
    public Object server(HttpServletRequest request, HttpServletResponse response);

}
