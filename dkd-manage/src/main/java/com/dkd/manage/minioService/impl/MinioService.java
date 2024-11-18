package com.dkd.manage.minioService.impl;


import com.dkd.common.core.domain.entity.SysMinio;
import com.dkd.manage.minioService.minio;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName MinioService
 * @Author DSF
 * @Date 2024年11月10日
 * @Description:
 */
@Service
public class MinioService implements minio {
    @Autowired
    private SysMinio sysMinio;

    @Override
    public String uploadFile(MultipartFile file) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(sysMinio.getEndpoint())
                .credentials(sysMinio.getAccessKey(), sysMinio.getSecretKey())
                .build();
        try {
            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(sysMinio.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(sysMinio.getBucketName()).build());
            } else {  // 如果存在打印信息
                System.out.println("Bucket 'dkd' already exists.");
            }
            //随机一个5位的uuid作为文件名


            String extFileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = new SimpleDateFormat("yyyyMMdd")
                    .format(new Date()) + "/" + UUID.randomUUID().toString().replace("-", "") + extFileName;
//                    .format(new Date()) + "/" + 1233 + "." + extFileName;
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(sysMinio.getBucketName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build();
            minioClient.putObject(putObjectArgs);
            return sysMinio.getEndpoint() + "/" + sysMinio.getBucketName() + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
