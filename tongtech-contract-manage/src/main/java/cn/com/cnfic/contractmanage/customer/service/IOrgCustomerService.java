package cn.com.cnfic.contractmanage.customer.service;

import cn.com.cnfic.contractmanage.customer.dto.OrgCustomerDTO;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 企业客户表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-11
 */
public interface IOrgCustomerService extends BaseService<OrgCustomer> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgCustomer
	 * @return
	 */
	IPage<OrgCustomerVO> selectOrgCustomerPage(IPage<OrgCustomerVO> page, OrgCustomerVO orgCustomer);

	/**
	 * 保存企业客户
	 * @param orgCustomer
	 * @return
	 */
	Boolean saveCustomer(OrgCustomerDTO orgCustomer);

	/**
	 * 查询详情
	 * @param orgCustomer
	 * @return
	 */
	OrgCustomerVO getOneDtail(OrgCustomer orgCustomer);

	/**
	 * 导出客户信息数据
	 * @param response
	 * @param orgCustomer 导出的数据实体
	 */
    void exportOrgCustomerList(HttpServletResponse response, OrgCustomerVO orgCustomer);


	/**
	 * 下载客户信息模板
	 * @param response
	 * @param key
	 */
    void downloadCustomerTemplate(HttpServletResponse response, String key);

    R imporCustomerFile(MultipartFile file);

	/**
	 * 获取redis导入的数据
	 * @param redisKey
	 * @param isSuccess
	 * @param query
	 * @return
	 */
    R getImportData(String redisKey, Boolean isSuccess, Query query);

	/**
	 * 导入客户数据
	 * @param redisKey
	 * @return
	 */
    R submitpassdata(String redisKey);

	/**
	 * 验证客户名称是否有重复
	 * @param custName
	 * @param custCode
     * @return true 没有重复,false 有重复的
	 */
	R<OrgCustomer> verifCustName(String custName, String custCode);

	/**
	 * 根据客户Id 删除客户
	 * @param ids id集合 多个用,隔开
	 * @return R
	 */
    R deleteByCustomerId(String ids);

	/**
	 * 自定义查询列表
	 * @param orgCustomer
	 * @return
	 */
    List<OrgCustomer> customList(OrgCustomer orgCustomer);

    Object companySearch(String name,String pageNo,String pageSize);
}
