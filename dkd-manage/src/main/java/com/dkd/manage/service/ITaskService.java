package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;

/**
 * 工单管理Service接口
 * 
 * @author dsf
 * @date 2024-11-13
 */
public interface ITaskService 
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
     * 批量删除工单管理
     * 
     * @param taskIds 需要删除的工单管理主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     * 删除工单管理信息
     * 
     * @param taskId 工单管理主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 查询工单管理列表
     * @param task task
     * @return List<TaskVo>
     */
    public List<TaskVo> selectTaskVoList(Task task);

    /**
     * 新增运营、运维工单
     * @param taskDto
     * @return 结果
     */
    int insertTaskDto(TaskDto taskDto);

    /**
     * 取消工单
     * @param task 工单
     * @return 结果
     */
    int cancelTask(Task task);


}
