package cn.com.cnfic.contractmanage.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.cnfic.contractmanage.contract.dto.UserAgreementDTO;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreement;
import cn.com.cnfic.contractmanage.contract.vo.UserAgreementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户协议表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
public interface UserAgreementMapper extends BaseMapper<UserAgreement> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userAgreement
	 * @return
	 */
	List<UserAgreementVO> selectUserAgreementPage(IPage page, @Param("userAgreement")UserAgreementDTO userAgreement);

}
