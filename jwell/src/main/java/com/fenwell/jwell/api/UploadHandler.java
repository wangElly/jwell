package com.fenwell.jwell.api;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UploadHandler {

    /**
     * 上传成功！
     */
    public static final int ERROR_CODE_SUCCESS = 10000;

    /**
     * 未包含文件上传域
     */
    public static final int ERROR_CODE_NOFILE = 10001;

    /**
     * 不允许的文件格式
     */
    public static final int ERROR_CODE_TYPE = 10002;

    /**
     * 文件大小超出限制
     */
    public static final int ERROR_CODE_SIZE = 10003;

    /**
     * 请求类型ENTYPE错误
     */
    public static final int ERROR_CODE_ENTYPE = 10004;

    /**
     * 上传请求异常
     */
    public static final int ERROR_CODE_REQUEST = 10005;

    /**
     * IO异常
     */
    public static final int ERROR_CODE_IO = 10006;

    /**
     * 目录创建失败
     */
    public static final int ERROR_CODE_DIR = 10007;

    /**
     * 未知错误
     */
    public static final int ERROR_CODE_UNKNOWN = 10008;

    public List<File> upload(HttpServletRequest request, String name);

}
