package com.fenwell.jwell.api;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptors {

    public Object server(HttpServletRequest request, HttpServletResponse response, Method method);

}
