package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Sku;
import lombok.Data;

/**
 * @ClassName ChannelVo
 * @Author DSF
 * @Date 2024年11月13日
 * @Description:
 */
@Data
public class ChannelVo extends Channel {
    //商品
    private Sku sku;
}
