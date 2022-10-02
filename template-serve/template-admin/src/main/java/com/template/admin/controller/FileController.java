package com.template.admin.controller;

import com.template.admin.dto.FileDelete;
import com.template.admin.service.FileService;
import com.template.common.api.CommonResult;
import org.apache.commons.fileupload.FileUpload;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@RestController
@RequestMapping("file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequiresPermissions(value = {"25", "*"}, logical = Logical.OR)
    @PostMapping("/minio/upload")
    public CommonResult<?> minioUpload(@RequestPart MultipartFile file, HttpServletRequest request) {
        Map map = fileService.minioUpload(file, request);
        if (!ObjectUtils.isEmpty(map)) {
            return CommonResult.success(map);
        }
        return CommonResult.failed();
    }

    @RequiresPermissions(value = {"26", "*"}, logical = Logical.OR)
    @PostMapping("/minio/delete")
    public CommonResult<?> minioDelete(@RequestBody @Validated FileDelete fileDelete, BindingResult bindingResult) {
        return fileService.minioDelete(fileDelete.getFolderName(), fileDelete.getName()) ? CommonResult.success() : CommonResult.failed();
    }

    @RequiresPermissions(value = {"27", "*"}, logical = Logical.OR)
    @GetMapping("/minio/download")
    public void minioDownload(@RequestParam String folderName, @RequestParam String name, HttpServletResponse response) {
        fileService.minioDownload(folderName, name, response);
    }
}
