package com.fenwell.jwell.spi.upload;

import javax.servlet.http.HttpServletRequest;

import com.fenwell.jwell.api.UploadHandler;
import com.fenwell.jwell.spi.pojo.FileMeta;

public class FileUploadHandler implements UploadHandler {

    public FileMeta upload(HttpServletRequest request, String name) {
        String filePath = request.getParameter(name);
        System.out.println(filePath);
        return null;
    }

}
