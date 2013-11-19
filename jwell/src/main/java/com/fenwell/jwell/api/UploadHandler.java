package com.fenwell.jwell.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fenwell.jwell.spi.pojo.FileMeta;

public interface UploadHandler {

    public List<FileMeta> upload(HttpServletRequest request, String name);

}
