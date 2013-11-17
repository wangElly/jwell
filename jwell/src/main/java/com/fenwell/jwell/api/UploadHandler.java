package com.fenwell.jwell.api;

import javax.servlet.http.HttpServletRequest;

import com.fenwell.jwell.spi.pojo.FileMeta;

public interface UploadHandler {

    public FileMeta upload(HttpServletRequest request, String name);

}
