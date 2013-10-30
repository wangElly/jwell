package com.fenwell.jwell.mvc.api;

public interface Loader {

    public <T> T load(Class<T> t);

}
