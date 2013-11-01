package com.fenwell.jwell.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.annotation.Param;

public interface ParamHandler {
    
    public Object execute(HttpServletRequest request, HttpServletResponse response, Class<?> t,
            Param param);

}
