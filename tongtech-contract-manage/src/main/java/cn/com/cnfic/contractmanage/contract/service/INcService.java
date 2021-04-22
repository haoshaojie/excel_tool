package cn.com.cnfic.contractmanage.contract.service;

import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.contract.vo.NcVO;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.com.cnfic.contractmanage.contract.dto.NcDTO;
import cn.com.cnfic.contractmanage.contract.entity.Nc;

/**
 * 用友合同表? 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface INcService extends BaseService<Nc> {

	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param nc
	 * @return
	 */
	IPage<NcVO> selectNcPage(Query query, NcDTO nc);

	/**
	 * 查询合同的详情信息
	 * @param id 合同id字段
	 * @return 合同
	 */
	NcVO getDetail(Long id);

}
