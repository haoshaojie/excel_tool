package cn.com.cnfic.contractmanage.contract.service;

import cn.com.cnfic.contractmanage.contract.dto.ContractDTO;
import cn.com.cnfic.contractmanage.contract.dto.UserAgreementDTO;
import cn.com.cnfic.contractmanage.contract.entity.Contract;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 合约表服务类
 *
 * @author zhaijw
 * @since 2021-03-23
 */
public interface IContractService extends BaseService<Contract> {

	/**
	 * 分页查询
	 *
	 * @param query 分页查询信息
	 * @param contractDto 合约管理查询条件数据封装类
	 * @return 分页contract结果
	 */
	IPage<ContractVO> selectContractPage(Query query, ContractDTO contractDto);

	/**
	 * 根据客户联系人id查询合约
	 * @param custContactId 客户联系人id
	 * @return 关联此客户联系人的合约集合
	 */
	List<ContractVO> findByCustContactId(Long custContactId);

	/**
	 * 根据客户id查询合约
	 * @param custId 客户id
	 * @return 关联此客户的合约集合
	 */
	List<ContractVO> findByCustId(Long custId);

	/**
	 * 新增合约-基本信息
	 *
	 * @param contractDto
	 * @return
	 */
	R<Contract> saveBasicInfo(ContractDTO contractDto);

	/**
	 * 新增合约-关联用户协议
	 *
	 * @param contractDto
	 * @return
	 */
    R saveUserAgreementInfo(ContractDTO contractDto);

	/**
	 * 删除合约
	 *
	 * @param contractDto
	 * @return
	 */
    R removeContract(ContractDTO contractDto);

	/**
	 * 导出合约
	 *
	 * @param response
	 * @param contractDto
	 * @return
	 */
	void exportContractList(HttpServletResponse response, ContractDTO contractDto);
}
