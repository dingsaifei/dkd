package com.dkd.manage.mapper;

import java.util.List;
import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.vo.TaskVo;

/**
 * 工单管理Mapper接口
 * 
 * @author dsf
 * @date 2024-11-13
 */
public interface TaskMapper 
{
    /**
     * 查询工单管理
     * 
     * @param taskId 工单管理主键
     * @return 工单管理
     */
    public Task selectTaskByTaskId(Long taskId);

    /**
     * 查询工单管理列表
     * 
     * @param task 工单管理
     * @return 工单管理集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增工单管理
     * 
     * @param task 工单管理
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改工单管理
     * 
     * @param task 工单管理
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除工单管理
     * 
     * @param taskId 工单管理主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 批量删除工单管理
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     * 查询工单管理列表
     * @param task task
     * @return List<TaskVo>
     */
    public List<TaskVo> selectTaskVoList(Task task);

}
