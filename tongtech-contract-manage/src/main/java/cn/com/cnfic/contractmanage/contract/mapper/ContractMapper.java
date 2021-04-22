package cn.com.cnfic.contractmanage.contract.mapper;

import cn.com.cnfic.contractmanage.contract.dto.ContractDTO;
import cn.com.cnfic.contractmanage.contract.entity.Contract;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelConContactVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelConUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合约表Mapper接口
 *
 * @author zhaijw
 * @since 2021-03-23
 */
public interface ContractMapper extends BaseMapper<Contract> {

	/**
	 * 合约管理分页查询
	 * @param page 分页信息
	 * @param contractDto 查询封装实体类
	 * @param depIds 当前登录人数据权限id集合
	 * @return 合约数据分页信息
	 */
	IPage<ContractVO> selectContractPage(IPage<ContractDTO> page, @Param("con") ContractDTO contractDto, @Param("depIds") List<String> depIds);

	/**
	 * 根据客户联系人id查询合约
	 * @param custContactId 客户联系人id
	 * @return 关联此客户联系人的合约集合
	 */
	List<ContractVO> selectByCustContactId(@Param("custContactId") Long custContactId);

	/**
	 * 根据客户id查询合约
	 * @param custId 客户id
	 * @return 关联此客户的合约集合
	 */
	List<ContractVO> selectByCustId(@Param("custId") Long custId);

	/**
	 * 根据合约id标识，查询合约绑定用户及产品情况
	 * @param contractIds 合约标识
	 * @return 关联此客户的合约集合
	 */
	List<ContractVO> getContractProUserInfo(List<Long> contractIds);

	/**
	 * 根据合约id标识，更新合约表及用友合同表
	 * @param contractIds 合约标识
	 * @return 关联此客户的合约集合
	 */
	int updateContractAndNc(List<Long> contractIds);

	/**
	 * 筛选导出合约信息
	 * @param contractDto 合约查询条件
	 * @return 合约信息
	 */
	List<ContractVO> selectContractInfo(@Param("con") ContractDTO contractDto);

	/**
	 * 筛选导出合约下联系信息
	 * @param contractIds 合约标识Contact
	 * @return 合约信息
	 */
	List<ExcelConContactVO> selectExcelConContract(List<Long> contractIds);

	/**
	 * 筛选导出合约下用户信息
	 * @param contractIds 合约标识
	 * @return 合约信息
	 */
	List<ExcelConUserVO> selectExcelContractUsers(List<Long> contractIds);
}
