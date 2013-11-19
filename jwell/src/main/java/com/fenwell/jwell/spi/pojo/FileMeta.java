package com.fenwell.jwell.spi.pojo;

import java.io.File;
import java.net.URI;

public class FileMeta extends File {

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

    private static final long serialVersionUID = 8024939781523373969L;

    public FileMeta(File parent, String child) {
        super(parent, child);
    }

    public FileMeta(String parent, String child) {
        super(parent, child);
    }

    public FileMeta(String pathname) {
        super(pathname);
    }

    public FileMeta(URI uri) {
        super(uri);
    }

    private String originalName;

    private String fileType;

    private int size;

    private int errcode;

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

}
