package cn.com.cnfic.contractmanage.contract.mapper;

import java.util.List;

import cn.com.cnfic.contractmanage.contract.vo.NcVO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.com.cnfic.contractmanage.contract.dto.NcDTO;
import cn.com.cnfic.contractmanage.contract.entity.Nc;

/**
 * 用友合同表? Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface NcMapper extends BaseMapper<Nc> {

	/**
	 *
	 * @param page 分页信息
	 * @param NcDTO 合同查询条件对象
	 * @param ids 当前登录人的部门数据权限
	 * @return
	 */
	IPage<NcVO> selectNcPageData(IPage<NcDTO> page, @Param("nc") NcDTO NcDTO, @Param("ids") List<String> ids);

	/**
	 * 查询合同信息详情
	 * @param id 合同id
	 * @param depIds 当前登录人的部门数据权限
	 * @return
	 */
	NcVO selectDetailById(@Param("id") Long id,@Param("depIds") List<String> depIds);
}
