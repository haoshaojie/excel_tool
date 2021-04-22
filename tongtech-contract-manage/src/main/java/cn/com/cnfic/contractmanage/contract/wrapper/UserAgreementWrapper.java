package cn.com.cnfic.contractmanage.contract.wrapper;


import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreement;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreementFile;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementFileService;
import cn.com.cnfic.contractmanage.contract.vo.UserAgreementVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 用户协议表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
public class UserAgreementWrapper extends BaseEntityWrapper<UserAgreement, UserAgreementVO> {

    public static UserAgreementWrapper build() {
        return new UserAgreementWrapper();
    }

	@Override
	public UserAgreementVO entityVO(UserAgreement userAgreement) {
		UserAgreementVO userAgreementVO = BeanUtil.copy(userAgreement, UserAgreementVO.class);
		return userAgreementVO;
	}

}
