package com.fenwell.jwell.spi.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fenwell.jwell.Mvcs;
import com.fenwell.jwell.api.UploadHandler;
import com.fenwell.jwell.spi.pojo.FileMeta;
import com.fenwell.util.Arrays;
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
    private int maxPoolSize = 10;

    /**
     * 每次清理文件池的数量
     */
    private int clearPoolSize = 8;

    public FileUploadHandler() {
        super();
        if (allowFiles == null) {
            allowFiles = new HashSet<String>();
            for (int i = 0; i < defultFiles.length; i++) {
                allowFiles.add(defultFiles[i]);
            }
        }
    }

    public List<File> upload(HttpServletRequest request, String name) {
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
        List<File> files = null;
        try {
            files = execute(factory, request, name, savePath);
        } catch (SizeLimitExceededException e) {
            files = errorResult(UploadHandler.ERROR_CODE_SIZE);
        } catch (InvalidContentTypeException e) {
            files = errorResult(UploadHandler.ERROR_CODE_ENTYPE);
        } catch (FileUploadException e) {
            files = errorResult(UploadHandler.ERROR_CODE_REQUEST);
        } catch (IOException e) {
            files = errorResult(UploadHandler.ERROR_CODE_IO);
        } catch (Exception e) {
            files = errorResult(UploadHandler.ERROR_CODE_UNKNOWN);
        }
        clearFilePool(savePath);
        return files;
    }

    /**
     * 清理文件池中的临时文件
     * 
     * @param savePath
     */
    private void clearFilePool(String savePath) {
        File file = new File(savePath);
        String[] lists = file.list();
        if (Arrays.isEmpty(lists)) {
            return;
        }
        if (lists.length < maxPoolSize) {
            return;
        }
        String[] delList = getDeleteFileNames(lists);
        if (delList != null) {
            for (int i = 0; i < delList.length; i++) {
                String path = savePath + File.separator + delList[i];
                File f = new File(path);
                f.delete();
            }
        }
    }

    private String[] getDeleteFileNames(String[] lists) {
        long[] intList = new long[lists.length];
        Map<Long, String> fileMap = new HashMap<Long, String>();
        for (int i = 0; i < intList.length; i++) {
            intList[i] = fileName2Int(lists[i]);
            fileMap.put(intList[i], lists[i]);
        }
        java.util.Arrays.sort(intList);
        String[] dl = new String[clearPoolSize];
        for (int i = 0; i < clearPoolSize; i++) {
            dl[i] = fileMap.get(intList[i]);
        }
        return dl;
    }

    private long fileName2Int(String file) {
        int lastDot = file.lastIndexOf(".");
        String name = null;
        if (lastDot == -1) {
            name = file;
        } else {
            name = file.substring(0, lastDot);
        }
        return Long.parseLong(name);
    }

    private List<File> errorResult(int errorCodeSize) {
        List<File> files = new ArrayList<File>();
        FileMeta fileMeta = new FileMeta(Strings.EMPTY);
        fileMeta.setErrcode(errorCodeSize);
        files.add(fileMeta);
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

    private List<File> execute(DiskFileItemFactory factory, HttpServletRequest request,
            String name, String savePath) throws FileUploadException, IOException {
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setSizeMax(this.maxSize * 1024);
        sfu.setHeaderEncoding(this.encode);
        FileItemIterator fii = sfu.getItemIterator(request);
        List<File> files = new ArrayList<File>();
        while (fii.hasNext()) {
            FileItemStream fis = fii.next();
            if (!fis.isFormField()) {
                String fieldName = fis.getFieldName();
                if (!fieldName.equals(name)) {
                    continue;
                }
                String fileType = getType(fis.getName());
                String originalName = fis.getName();
                String fileName = System.currentTimeMillis() + "." + fileType;
                if (!allowFiles.contains(fileType)) {
                    FileMeta fm = new FileMeta(Strings.EMPTY);
                    fm.setFileType(fileType);
                    fm.setOriginalName(originalName);
                    files.add(fm);
                    continue;
                }
                String uri = savePath + File.separator + fileName;
                BufferedInputStream in = new BufferedInputStream(fis.openStream());
                int size = in.available();
                FileOutputStream out = new FileOutputStream(new File(uri));
                BufferedOutputStream output = new BufferedOutputStream(out);
                Streams.copy(in, output, true);

                FileMeta fm = new FileMeta(uri);
                fm.setFileType(fileType);
                fm.setOriginalName(originalName);
                fm.setSize(size);
                fm.setErrcode(UploadHandler.ERROR_CODE_SUCCESS);
                fm.setUri(uri);

                files.add(fm);
            }
        }
        return files;
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
