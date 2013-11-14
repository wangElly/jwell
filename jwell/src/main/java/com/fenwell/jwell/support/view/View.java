package com.fenwell.jwell.support.view;

import java.util.HashMap;
import java.util.Map;

public class View {

    private String page;

    private Map<String, Object> param;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

}
