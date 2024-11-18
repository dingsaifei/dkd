package com.dkd.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dkd.common.constant.DkdContants;
import com.dkd.common.utils.DateUtils;
import com.dkd.common.utils.uuid.UUIDUtils;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.VmType;
import com.dkd.manage.mapper.ChannelMapper;
import com.dkd.manage.service.IChannelService;
import com.dkd.manage.service.INodeService;
import com.dkd.manage.service.IVmTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.VendingMachineMapper;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备管理Service业务层处理
 *
 * @author dsf
 * @date 2024-11-11
 */
@Service
public class VendingMachineServiceImpl implements IVendingMachineService {
    @Autowired
    private VendingMachineMapper vendingMachineMapper;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private IVmTypeService vmTypeService;
    @Autowired
    private INodeService nodeService;

    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public VendingMachine selectVendingMachineById(Long id) {
        return vendingMachineMapper.selectVendingMachineById(id);
    }

    /**
     * 查询设备管理列表
     *
     * @param vendingMachine 设备管理
     * @return 设备管理
     */
    @Override
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine) {
        return vendingMachineMapper.selectVendingMachineList(vendingMachine);
    }

    /**
     * 新增设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVendingMachine(VendingMachine vendingMachine) {
        //新增设备
        //1-1生成8位的唯一标识，补充货道编号
        String innerCode = UUIDUtils.getUUID();
        vendingMachine.setInnerCode(innerCode);
        //1-2查询售货机类型表，获取货道数量
        VmType vmType = vmTypeService.selectVmTypeById(vendingMachine.getVmTypeId());
        vendingMachine.setChannelMaxCapacity(vmType.getChannelMaxCapacity());
        //1-3 查询点位表，补充：区域，点位，合作商
        Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
        BeanUtils.copyProperties(node, vendingMachine, "id");
        vendingMachine.setAddr(node.getAddress());
        //1-4设备状态
        vendingMachine.setVmStatus(DkdContants.VM_STATUS_NODEPLOY);//默认未投放
        vendingMachine.setCreateTime(DateUtils.getNowDate());
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        //1-5保存设备
        int result = vendingMachineMapper.insertVendingMachine(vendingMachine);
        //2.新增货道
        //2-1生成货道列表
        List<Channel> channelList = new ArrayList<>();
        //2-2 双层循环，生成货道
        for (int i = 1; i <= vmType.getVmRow(); i++) {
            for (int j = 1; j <= vmType.getVmCol(); j++) {
                //2-3封装货到对象
                Channel channel = new Channel();
                channel.setChannelCode(i + "-" + j);//货道编号
                channel.setVmId(vendingMachine.getId());//售货机ID
                channel.setInnerCode(vendingMachine.getInnerCode());//设备内部编号
                channel.setMaxCapacity(vendingMachine.getChannelMaxCapacity());//货道最大容量
                channel.setCreateTime(DateUtils.getNowDate());
                channel.setUpdateTime(DateUtils.getNowDate());
                channelList.add(channel);
                //保存货道
                //channelService.insertChannel(channel);
            }
        }
        //2-4批量保存货道
        channelService.batchInsertChannels(channelList);
        return result;
    }

    /**
     * 修改设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    public int updateVendingMachine(VendingMachine vendingMachine) {
       if(vendingMachine.getNodeId()!=null){
           //查询点位表，补充：区域，点位，合作商
           Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
           //补充：区域，点位，合作商，点位地址
           BeanUtils.copyProperties(node, vendingMachine, "id");
           vendingMachine.setAddr(node.getAddress());
       }
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        return vendingMachineMapper.updateVendingMachine(vendingMachine);
    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineByIds(Long[] ids) {
        return vendingMachineMapper.deleteVendingMachineByIds(ids);
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineById(Long id) {
        return vendingMachineMapper.deleteVendingMachineById(id);
    }

    @Override
    public VendingMachine selectVendingMachineByInnerCode(String innerCode) {
        return vendingMachineMapper.selectVendingMachineByInnerCode(innerCode);
    }
}
