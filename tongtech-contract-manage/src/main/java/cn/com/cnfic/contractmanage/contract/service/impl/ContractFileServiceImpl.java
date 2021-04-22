package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.contract.entity.ContractFile;
import cn.com.cnfic.contractmanage.contract.mapper.ContractFileMapper;
import cn.com.cnfic.contractmanage.contract.service.IContractFileService;
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
public class ContractFileServiceImpl extends BaseServiceImpl<ContractFileMapper, ContractFile> implements IContractFileService {

    @Override
    public List<ContractFile> getContractFileListByContId(Long contId) {
        QueryWrapper<ContractFile> wrapper = new QueryWrapper<ContractFile>();
        wrapper.eq("cont_id", contId);
        return list(wrapper);
    }
}
