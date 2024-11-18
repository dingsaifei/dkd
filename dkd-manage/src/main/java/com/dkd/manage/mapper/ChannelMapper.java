package com.dkd.manage.mapper;

import java.util.List;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.vo.ChannelVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 售货机货道Mapper接口
 * 
 * @author dsf
 * @date 2024-11-11
 */
public interface ChannelMapper 
{
    /**
     * 查询售货机货道
     * 
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    public Channel selectChannelById(Long id);

    /**
     * 查询售货机货道列表
     * 
     * @param channel 售货机货道
     * @return 售货机货道集合
     */
    public List<Channel> selectChannelList(Channel channel);

    /**
     * 新增售货机货道
     * 
     * @param channel 售货机货道
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改售货机货道
     * 
     * @param channel 售货机货道
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 删除售货机货道
     * 
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量删除售货机货道
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);


    /**
     * 批量新增
     * @param channelList channelList
     * @return 影响的行数
     */
    public int batchInsertChannels(List<Channel> channelList);

    /**
     * 根据skuIds查询货道数量
     * @param skuIds
     * @return
     */
    public int countChannelBySkuIds(Long[] skuIds);


    /**
     * 查询货道列表
     * @param innerCode innerCode
     * @return  ChannelVo集合
     */
    public List<ChannelVo> selectChannelVoListByInnerCode(String innerCode);


    /**
     * 根据innerCode和channelCode查询货道信息
     * @param innerCode innerCode
     * @param channelCode channelCode
     * @return Channel对象
     */
    @Select("select * from tb_channel where inner_code = #{innerCode} and channel_code = #{channelCode}")
    public Channel getChannelInfo(@Param("innerCode") String innerCode,@Param("channelCode") String channelCode);

    /**
     * 批量修改货道信息
     * @param channelList
     * @return
     */
    public int batchUpdateChannels(List<Channel> channelList);

}
