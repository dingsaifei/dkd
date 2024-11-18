package com.dkd.manage.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.domain.dto.TaskDetailsDto;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;
import com.dkd.manage.service.IEmpService;
import com.dkd.manage.service.ITaskDetailsService;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.TaskMapper;
import com.dkd.manage.domain.Task;
import com.dkd.manage.service.ITaskService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单管理Service业务层处理
 *
 * @author dsf
 * @date 2024-11-13
 */
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private IVendingMachineService vendingMachineService;

    @Autowired
    private IEmpService empService;

    @Autowired
    private ITaskDetailsService taskDetailsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询工单管理
     *
     * @param taskId 工单管理主键
     * @return 工单管理
     */
    @Override
    public Task selectTaskByTaskId(Long taskId) {
        return taskMapper.selectTaskByTaskId(taskId);
    }

    /**
     * 查询工单管理列表
     *
     * @param task 工单管理
     * @return 工单管理
     */
    @Override
    public List<Task> selectTaskList(Task task) {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增工单管理
     *
     * @param task 工单管理
     * @return 结果
     */
    @Override
    public int insertTask(Task task) {
        task.setCreateTime(DateUtils.getNowDate());
        return taskMapper.insertTask(task);
    }

    /**
     * 修改工单管理
     *
     * @param task 工单管理
     * @return 结果
     */
    @Override
    public int updateTask(Task task) {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除工单管理
     *
     * @param taskIds 需要删除的工单管理主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskIds(Long[] taskIds) {
        return taskMapper.deleteTaskByTaskIds(taskIds);
    }

    /**
     * 删除工单管理信息
     *
     * @param taskId 工单管理主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskId(Long taskId) {
        return taskMapper.deleteTaskByTaskId(taskId);
    }

    /**
     * 查询工单管理列表
     *
     * @param task task
     * @return
     */
    @Override
    public List<TaskVo> selectTaskVoList(Task task) {
        return taskMapper.selectTaskVoList(task);
    }

    /**
     * 新增工单管理
     *
     * @param taskDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTaskDto(TaskDto taskDto) {
        //1.查询售货机是否存在
        VendingMachine vm = vendingMachineService.selectVendingMachineByInnerCode(taskDto.getInnerCode());
        if (vm == null) {
            throw new ServiceException("设备不存在");
        }
        //2.校验售货机状态与工单状态是否相符
        checkCreateTask(vm.getVmStatus(), taskDto.getProductTypeId());
        //3. 检查设备是否有未完成的同类型工单
        hasTask(taskDto);

        //4.校验员工是否存在
        Emp emp = empService.selectEmpById(taskDto.getUserId());
        if (emp == null) {
            throw new ServiceException("员工不存在");
        }

        //5.检验工区域是否匹配
        if (!emp.getRegionId().equals(vm.getRegionId())) {
            throw new ServiceException("员工区域与设备区域不一致，无法处理此工单");
        }
        Task task = BeanUtil.copyProperties(taskDto, Task.class);
        task.setTaskStatus(DkdContants.TASK_STATUS_CREATE);//创建工单
        task.setUserName(emp.getUserName());//执行人名称
        task.setRegionId(vm.getRegionId());//区域ID
        task.setAddr(vm.getAddr());//设备地址
        task.setCreateTime(DateUtils.getNowDate());//创建时间
        task.setTaskCode(generateTaskCode());//工单编号
        int taskResult = taskMapper.insertTask(task);
        //7. 判断是否为补货工单
        if (taskDto.getProductTypeId().equals(DkdContants.TASK_TYPE_SUPPLY)) {
            //8.保存工单详情
            List<TaskDetailsDto> details = taskDto.getDetails();
            if (CollUtil.isEmpty(details)) {
                throw new ServiceException("补货工单详情不能为空");
            }
            // 将dto转为po补充属性
            List<TaskDetails> taskDetailsList = details.stream().map(dto -> {
                TaskDetails taskDetails = BeanUtil.copyProperties(dto, TaskDetails.class);
                taskDetails.setTaskId(task.getTaskId());
                return taskDetails;
            }).collect(Collectors.toList());
            //9.批量新增
            taskDetailsService.batchInsertTaskDetails(taskDetailsList);
        }
        return taskResult;
    }

    @Override
    public int cancelTask(Task task) {
        Task taskDb = taskMapper.selectTaskByTaskId(task.getTaskId());
        //1.判断工单状态时候可以取消
        if(taskDb.getTaskStatus().equals(DkdContants.TASK_STATUS_FINISH)){
            throw new ServiceException("工单已完成，无法取消");
        }
        if(taskDb.getTaskStatus().equals(DkdContants.TASK_STATUS_CANCEL)){
            throw new ServiceException("工单已取消，无法再次取消");
        }
        //2.设置更新字段,状态 ， ID
        task.setTaskStatus(DkdContants.TASK_STATUS_CANCEL);
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    //生成并获取当天工单编号
    private String generateTaskCode() {
        //获取当前日期
        String dateStr = DateUtils.getDate().replaceAll("-", "");
        String key = "dkd.task.code." + dateStr;
        if (!redisTemplate.hasKey(key)) {
            //不存在设置初始值
            redisTemplate.opsForValue().set(key, 1, Duration.ofDays(1));
            return dateStr + "0001";
        }
        return dateStr + StrUtil.padPre(redisTemplate.opsForValue().increment(key).toString(), 4, '0');
    }

    private void hasTask(TaskDto taskDto) {
        Task taskParam = new Task();
        taskParam.setInnerCode(taskDto.getInnerCode());
        taskParam.setProductTypeId(taskDto.getProductTypeId());
        taskParam.setTaskStatus(DkdContants.TASK_STATUS_PROGRESS);
        //查询数据库中符合指定条件的工单列表
        List<Task> taskList = taskMapper.selectTaskList(taskParam);
        //如果存在未完成的同类型工单，则不能创建新的工单
        if (CollUtil.isNotEmpty(taskList)) {
            throw new ServiceException("该设备存在未完成的工单，请等待处理");
        }
    }

    /**
     * 检查创建工单
     *
     * @param vmStatus
     * @param productTypeId
     */
    private void checkCreateTask(Long vmStatus, Long productTypeId) {
        if (productTypeId == DkdContants.TASK_TYPE_DEPLOY && vmStatus == DkdContants.VM_STATUS_RUNNING) {
            throw new ServiceException("该设备状态为运行中，无法进行投放");
        }
        if (productTypeId == DkdContants.TASK_TYPE_SUPPLY && vmStatus != DkdContants.VM_STATUS_RUNNING) {
            throw new ServiceException("该设备状态为运行中，无法进行补货");
        }
        if (productTypeId == DkdContants.TASK_TYPE_REPAIR && vmStatus != DkdContants.VM_STATUS_RUNNING) {

            throw new ServiceException("该设备状态为运行中，无法进行维修");
        }
        if (productTypeId == DkdContants.TASK_TYPE_REVOKE && vmStatus != DkdContants.VM_STATUS_RUNNING) {

            throw new ServiceException("该设备状态为运行中，无法进行撤机");
        }


    }
}
