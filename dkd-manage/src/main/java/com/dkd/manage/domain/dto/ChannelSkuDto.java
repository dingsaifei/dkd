package com.dkd.manage.domain.dto;

import lombok.Data;

/**
 * @ClassName ChannelSkuDto
 * @Author DSF
 * @Date 2024年11月13日
 * @Description:
 */
@Data
public class ChannelSkuDto {
    //设备编码
    private String innerCode;
    //货道编号
    private String channelCode;
    //商品Id
    private Long skuId;
}
