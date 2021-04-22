package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.common.util.StrRepUtil;
import cn.com.cnfic.contractmanage.contract.dto.NcDTO;
import cn.com.cnfic.contractmanage.contract.entity.Nc;
import cn.com.cnfic.contractmanage.contract.mapper.NcMapper;
import cn.com.cnfic.contractmanage.contract.service.INcService;
import cn.com.cnfic.contractmanage.contract.vo.NcVO;
import cn.com.cnfic.system.user.entity.UserInfo;
import cn.com.cnfic.system.user.feign.IUserClient;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用友合同服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@Service
@AllArgsConstructor
public class NcServiceImpl extends BaseServiceImpl<NcMapper, Nc> implements INcService {

    /**
     * 用户数据权限list的分隔符
     */
    private static final String DATA_AUTH_DIVISION = ",";

    private NcMapper ncMapper;

    private IUserClient userClient;

    @Override
    public IPage<NcVO> selectNcPage(Query query, NcDTO nc) {
        //获取当前登录人的id
        Long userId = SecureUtil.getUser().getUserId();
        //获取当前登录人的部门权限信息
        UserInfo user = userClient.userInfo(userId).getData();
        String dataAuths = user.getUser().getDataAuthority();
        //将部门权限信息转化为list集合
        List<String> list = StrRepUtil.strConvertList(dataAuths, DATA_AUTH_DIVISION);
        nc.setProdDeptIdList(list);
        IPage<NcDTO> page = Condition.getPage(query);
        return ncMapper.selectNcPageData(page, nc, list);
    }

    @Override
    public NcVO getDetail(Long id) {
        //获取当前登录人的id
        Long userId = SecureUtil.getUser().getUserId();
        //获取当前登录人的部门权限信息
        UserInfo user = userClient.userInfo(userId).getData();
        String dataAuths = user.getUser().getDataAuthority();
        //将部门权限信息转化为list集合
        List<String> list = StrRepUtil.strConvertList(dataAuths, DATA_AUTH_DIVISION);
        return ncMapper.selectDetailById(id, list);
    }
}
