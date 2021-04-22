package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.entity.ProdItemRelation;
import cn.com.cnfic.contractmanage.product.vo.ProdItemRelationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 产品与产品项关联表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-04-02
 */
public interface ProdItemRelationMapper extends BaseMapper<ProdItemRelation> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param prodItemRelation
	 * @return
	 */
	List<ProdItemRelationVO> selectProdItemRelationPage(IPage page, ProdItemRelationVO prodItemRelation);

	/**
	 * 根据产品项id 删除关联表
	 * @param id
	 * @return
	 */
	int removeByRelationId(@Param(value = "id") Long id);
}
