package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RegionVo
 * @Author DSF
 * @Date 2024年11月09日
 * @Description:
 */
@Data
public class RegionVo extends Region {
    //点位数
    private Integer nodeCount;


}
