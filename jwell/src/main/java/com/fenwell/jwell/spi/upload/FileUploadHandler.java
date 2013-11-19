package com.fenwell.jwell.spi.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.Mvcs;
import com.fenwell.jwell.api.UploadHandler;
import com.fenwell.jwell.spi.pojo.FileMeta;
import com.fenwell.util.Strings;

public class FileUploadHandler implements UploadHandler {

    private static final Log log = LogFactory.getLog(FileUploadHandler.class);

    /**
     * 临时文件存放目录
     */
    private String tmplFile = createTmplFile();

    /**
     * 文件大小限制，单位KB
     */
    private int maxSize = 1000;

    private String[] defultFiles = { "rar", "doc", "docx", "zip", "pdf", "txt", "swf", "wmv",
            "gif", "png", "jpg", "jpeg", "bmp" };

    /**
     * 文件允许格式
     */
    private Set<String> allowFiles;

    /**
     * HeaderEncoding
     */
    private String encode = "utf-8";

    /**
     * 文件池中最大文件数量
     */
    private int maxPoolSize = 1000;

    /**
     * 每次清理文件池的数量
     */
    private int clearPoolSize = 800;

    public FileUploadHandler() {
        super();
        if (allowFiles == null) {
            allowFiles = new HashSet<String>();
            for (int i = 0; i < defultFiles.length; i++) {
                allowFiles.add(defultFiles[i]);
            }
        }
    }

    public List<FileMeta> upload(HttpServletRequest request, String name) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            log.error("Does not contain a file upload domain !");
            return null;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String savePath = getFolder(tmplFile);
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        factory.setRepository(saveFile);
        List<FileMeta> files = null;
        try {
            files = execute(factory, request, name, savePath);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    private String createTmplFile() {
        StringBuffer path = new StringBuffer(32);
        path.append("upload_tmpl");
        for (int i = 0; i < 10; i++) {
            path.append("00");
            if (i != 10 - 1) {
                path.append(File.separator);
            }
        }
        return path.toString();
    }

    private List<FileMeta> execute(DiskFileItemFactory factory, HttpServletRequest request,
            String name, String savePath) throws FileUploadException, IOException {
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setSizeMax(this.maxSize * 1024);
        sfu.setHeaderEncoding(this.encode);
        FileItemIterator fii = sfu.getItemIterator(request);
        while (fii.hasNext()) {
            FileItemStream fis = fii.next();
            if (!fis.isFormField()) {
                String fieldName = fis.getFieldName();
                if (!fieldName.equals(name)) {
                    continue;
                }
                String fileType = getType(fis.getName());
                if (!allowFiles.contains(fileType)) {
                    
                }
                String originalName = fis.getName();
                String fileName = System.currentTimeMillis() + "." + fileType;
                String uri = savePath + File.separator + fileName;
                BufferedInputStream in = new BufferedInputStream(fis.openStream());
                int size = in.available();
            }
        }
        return null;
    }

    private String getType(String name) {
        if (Strings.isBlank(name)) {
            return Strings.EMPTY;
        }
        int lastDot = name.lastIndexOf(".");
        if (lastDot == -1) {
            return Strings.EMPTY;
        }
        return name.substring(++lastDot);
    }

    private String getFolder(String path) {
        return Mvcs.realPath() + path;
    }

    public void setTmplFile(String tmplFile) {
        this.tmplFile = tmplFile;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setAllowFiles(Set<String> allowFiles) {
        this.allowFiles = allowFiles;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setClearPoolSize(int clearPoolSize) {
        this.clearPoolSize = clearPoolSize;
    }

}
