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

}
