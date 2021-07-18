package com.duan.file.controller;

import com.duan.file.file.FastDFSFile;
import com.duan.file.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName FileController
 * @Author DuanJinFei
 * @Date 2021/3/31 19:29
 * @Version 1.0
 */
@RestController
@CrossOrigin // 禁止跨域请求
public class FileController {
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 将请求的文件封装
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getName(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename()));
        // 文件上传
        String[] uploads = FastDFSClient.upload(fastDFSFile);
        //组装文件上传地址
        return FastDFSClient.getTrackerUrl()+"/"+uploads[0]+"/"+uploads[1];
    }

}
