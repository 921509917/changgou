package com.duan.content.file;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

/**
 * @ClassName FastDFSTest
 * @Author DuanJinFei
 * @Date 2021/3/31 18:59
 * @Version 1.0
 */
public class FastDFSTest {
    @Test
    public void upload() throws Exception{
        // 加载全局配置文件
        ClientGlobal.init("E:\\SpringBootWorkspace\\changgou\\changgou-service\\changgou-service-file\\src\\main" +
                "\\resources\\fdfs_client.conf");
        // 创建TrackerClient客户端对象---追踪客户端，用于将文件传到StorageClient
        TrackerClient trackerClient = new TrackerClient();
        // 通过客户端对象获取TrackerServer对象
        TrackerServer connection = trackerClient.getConnection();
        // 获取StorageClient对象---存储客户端
        StorageClient storageClient = new StorageClient(connection,null);
        String[] jpgs = storageClient.upload_file("F:\\文件\\畅购-讲师源码md课件\\day02\\讲义\\image\\1-1.png","png",null);

        for (String jpg : jpgs){
            System.out.println(jpgs);
        }
    }
}
