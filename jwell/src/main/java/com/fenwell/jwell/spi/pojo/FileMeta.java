package com.fenwell.jwell.spi.pojo;

import java.io.File;
import java.net.URI;

public class FileMeta extends File {

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

    private String uri;

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

    public String getOriginalName() {
        return originalName;
    }

    public String getFileType() {
        return fileType;
    }

    public int getSize() {
        return size;
    }

    public int getErrcode() {
        return errcode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
