package com.fenwell.jwell.spi.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenwell.jwell.api.UploadHandler;

public class FileUploadHandler implements UploadHandler {

    public File upload(HttpServletRequest request, HttpServletResponse resp) {
        return null;
    }

}
