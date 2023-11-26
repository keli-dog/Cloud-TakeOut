package com.cloud.controller.admin;

import com.cloud.constant.MessageConstant;
import com.cloud.result.Result;
import com.cloud.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "分类列表", notes = "分类列表")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件开始: file:{}", file);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectname = UUID.randomUUID().toString() + extension;
        String filePath = null;
        try {
            filePath = aliOssUtil.upload(file.getBytes(), objectname);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("上传文件异常:{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
