package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.Partner;
import com.dkd.manage.domain.Region;
import lombok.Data;

/**
 * @ClassName NodeVo
 * @Author DSF
 * @Date 2024年11月09日
 * @Description:
 */
@Data
public class NodeVo extends Node {
    //设备数量
    private Integer vmCount;
    //区域信息
    private Region region;
    //合作商信息
    private Partner partner;

}
