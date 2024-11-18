package com.dkd.manage.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ChannelConfigDto
 * @Author DSF
 * @Date 2024年11月13日
 * @Description:
 */
@Data
public class ChannelConfigDto {
    //设备号
    private  String innerCode;
    //货道配置集合
    private List<ChannelSkuDto> channelList;
}
