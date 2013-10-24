package com.fenwell.jwell.mvc.api;

import java.util.List;

public interface ScanHandler {

    public List<Class<?>> scan(List<String> pack);

}
