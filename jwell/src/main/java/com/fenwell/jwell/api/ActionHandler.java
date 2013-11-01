package com.fenwell.jwell.api;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionHandler {

    public void init() throws ServletException;

    public void dispatcher(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws Exception;

}
