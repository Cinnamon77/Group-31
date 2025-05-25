package com.bookkeeping.controller;

import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.core.api.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author bookkeeping
 **/
@Tag(name = "文件上传")
@RestController(value = "FileUploadController")
@RequestMapping(value = "api/upload")
public class FileUploadController {
    // 指定存储目录（示例：项目根目录下的 uploads 文件夹）
    private final Path rootLocation = Paths.get("uploads");

    @Operation(summary = "图片上传")
    @RequestMapping(value = "image", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                return CommonResult.failed(ResultCode.FILE_UPLOAD_IMG);
            }
            // 1. 创建存储目录（如果不存在）
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            // 2. 生成唯一文件名（避免覆盖）
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 3. 保存文件到本地
            Path destination = rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return CommonResult.success(filename);
        } catch (IOException e) {
            return CommonResult.failed(ResultCode.FILE_UPLOAD_ERROR);
        }
    }
}
