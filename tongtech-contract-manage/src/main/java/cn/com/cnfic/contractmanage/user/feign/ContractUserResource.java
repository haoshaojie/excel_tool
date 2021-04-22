package cn.com.cnfic.contractmanage.user.feign;

import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import cn.com.cnfic.contractmanage.user.service.IPersonalUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;

/**
 * @Description 合约管理-用户管理feign实现
 * @Author misterbig
 * @Date 2021/4/9
 */
@ApiIgnore
@AllArgsConstructor
@RestController
public class ContractUserResource implements ContractUserClient {

    private final IOrgUserService orgUserService;

    private final IPersonalUserService personalUserService;

    @Override
    public List<PersonalUser> listPersonalByAccId(List<String> accIds) {
        if (CollectionUtils.isEmpty(accIds)) {
            return Collections.emptyList();
        }
        return personalUserService.list(Wrappers.lambdaQuery(PersonalUser.class)
                .in(PersonalUser::getAccountId, accIds)
                .eq(PersonalUser::getIsDeleted, NumberUtils.INTEGER_ZERO));
    }

    @Override
    public List<OrgUser> listOrgByAccId(List<String> accIds) {
        if (CollectionUtils.isEmpty(accIds)) {
            return Collections.emptyList();
        }
        return orgUserService.list(Wrappers.lambdaQuery(OrgUser.class)
                .in(OrgUser::getAccountId, accIds)
                .eq(OrgUser::getIsDeleted, NumberUtils.INTEGER_ZERO));
    }
}
