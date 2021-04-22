package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.contract.entity.UserAgreementFile;
import cn.com.cnfic.contractmanage.contract.mapper.UserAgreementFileMapper;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementFileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户协议表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
@Service
public class UserAgreementFileServiceImpl extends BaseServiceImpl<UserAgreementFileMapper, UserAgreementFile> implements IUserAgreementFileService {

    @Override
    public List<UserAgreementFile> getUserAgreementFileLisstByAgreementId(Long agreementId) {
        //包装用户协议文件列表
        QueryWrapper<UserAgreementFile> wrapper = new QueryWrapper<>();
        wrapper.eq("agreement_id", agreementId);
        return list(wrapper);
    }
}
