package com.dkd.common.core.domain.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName Sysminio
 * @Author DSF
 * @Date 2024年11月10日
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class SysMinio {
    // 连接地址
    private String endpoint;
    // 账号
    private String accessKey;
    // 密码
    private String secretKey;
    // 存储桶名称
    private String bucketName;
}
