package com.dkd.manage.minioService;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName minio
 * @Author DSF
 * @Date 2024年11月10日
 * @Description:
 */
public interface minio {
    public String uploadFile(MultipartFile file);
}
