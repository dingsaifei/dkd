package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Partner;
import lombok.Data;

/**
 * @ClassName PartnerVo
 * @Author DSF
 * @Date 2024年11月09日
 * @Description:
 */
@Data
public class PartnerVo extends Partner {
    //合作商的点位数
    private Integer nodeCount;
}
