package com.dkd.manage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;
import com.dkd.manage.service.ITaskDetailsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dkd.common.annotation.Log;
import com.dkd.common.core.controller.BaseController;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.enums.BusinessType;
import com.dkd.manage.domain.Task;
import com.dkd.manage.service.ITaskService;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.common.core.page.TableDataInfo;

/**
 * 工单管理Controller
 *
 * @author dsf
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/manage/task")
public class TaskController extends BaseController {
    @Autowired
    private ITaskService taskService;
    @Autowired
    private ITaskDetailsService taskDetailsService;

    /**
     * 查询工单管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(Task task) {
        startPage();
//        List<Task> list = taskService.selectTaskList(task);
        List<TaskVo> list = taskService.selectTaskVoList(task);
        return getDataTable(list);
    }

    /**
     * 导出工单管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:task:export')")
    @Log(title = "工单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Task task) {
        List<Task> list = taskService.selectTaskList(task);
        ExcelUtil<Task> util = new ExcelUtil<Task>(Task.class);
        util.exportExcel(response, list, "工单管理数据");
    }

    /**
     * 获取工单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:task:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId) {
        return success(taskService.selectTaskByTaskId(taskId));
    }


    /*@PreAuthorize("@ss.hasPermi('manage:task:add')")
    @Log(title = "工单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Task task)
    {
        return toAjax(taskService.insertTask(task));
    }
*/

    /**
     * 新增工单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:task:add')")
    @Log(title = "工单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskDto taskDto) {
        //设置指派人id
        taskDto.setAssignorId(getUserId());
        return toAjax(taskService.insertTaskDto(taskDto));
    }

    /**
     * 修改工单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:task:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Task task) {
        return toAjax(taskService.updateTask(task));
    }

    /**
     * 删除工单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:task:remove')")
    @Log(title = "工单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(taskService.deleteTaskByTaskIds(taskIds));
    }


    /**
     * 取消工单
     */
    @PreAuthorize("@ss.hasPermi('manage:task:edit')")
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel")
    public AjaxResult cancelTask(@RequestBody Task task) {
        return toAjax(taskService.cancelTask(task));
    }

}
