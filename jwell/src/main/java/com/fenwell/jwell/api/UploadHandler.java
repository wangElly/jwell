package com.fenwell.jwell.api;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UploadHandler {

    public File upload(HttpServletRequest request, HttpServletResponse resp);

}
