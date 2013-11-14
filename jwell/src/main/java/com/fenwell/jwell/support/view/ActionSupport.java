package com.fenwell.jwell.support.view;

import java.util.HashMap;
import java.util.Map;

import com.fenwell.util.Arrays;

public class ActionSupport {

    public View view(String page, Map<String, Object> param) {
        View view = new View();
        view.setPage(page);
        view.setParam(param);
        return view;
    }

    public View view(String page) {
        View view = new View();
        view.setPage(page);
        return view;
    }

    public View view(String page, Param... param) {
        View view = new View();
        view.setPage(page);
        if (!Arrays.isEmpty(param)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            for (Param p : param) {
                paramMap.put(p.getKey(), p.getValue());
            }
            view.setParam(paramMap);
        }
        return view;
    }

    public Param p(String key, Object value) {
        return new Param(key, value);
    }

}
