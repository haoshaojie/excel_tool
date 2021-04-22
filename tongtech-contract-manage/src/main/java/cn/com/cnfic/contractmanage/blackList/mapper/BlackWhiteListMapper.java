package cn.com.cnfic.contractmanage.blackList.mapper;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.BlackWhiteListExport;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 黑白名单表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
public interface BlackWhiteListMapper extends BaseMapper<BlackWhiteList> {

    /**
     * 获取导出用户数据
     *
     * @param queryWrapper
     * @return
     */
    List<BlackWhiteListExport> selectExport(@Param(Constants.WRAPPER) Wrapper<BlackWhiteList> queryWrapper);

    /**
     * 自定义分页
     *
     * @param page
     * @param blackWhiteList
     * @return
     */
    List<BlackWhiteListVO> selectBlackWhiteListPage(IPage page, BlackWhiteListVO blackWhiteList);

}
