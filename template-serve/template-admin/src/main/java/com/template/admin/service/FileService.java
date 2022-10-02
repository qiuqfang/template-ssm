package com.template.admin.service;

import org.apache.commons.fileupload.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public interface FileService {

    Map minioUpload(MultipartFile file, HttpServletRequest request);

    Boolean minioDelete(String folderName, String name);

    void minioDownload(String folderName, String name, HttpServletResponse response);

}
