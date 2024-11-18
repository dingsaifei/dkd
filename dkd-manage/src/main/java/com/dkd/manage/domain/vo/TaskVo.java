package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.TaskType;
import lombok.Data;

/**
 * @ClassName TaskCo
 * @Author DSF
 * @Date 2024年11月13日
 * @Description:
 */
@Data
public class TaskVo extends Task {
    //工单类型
    private TaskType taskType;
}
