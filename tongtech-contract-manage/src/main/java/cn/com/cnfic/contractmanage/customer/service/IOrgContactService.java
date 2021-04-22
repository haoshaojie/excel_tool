package cn.com.cnfic.contractmanage.customer.service;

import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.vo.OrgContactVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 企业客户联系人表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public interface IOrgContactService extends BaseService<OrgContact> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgContact
	 * @return
	 */
	IPage<OrgContactVO> selectOrgContactPage(IPage<OrgContactVO> page, OrgContactVO orgContact);
	/**
	 * 通过客户Id 批量逻辑删除
	 * @param id
	 */
    void deleteLogicByCustomerId(Long id);

	/**
	 * 通过客户Id 集合查询联系人信息
	 * @param customerIds
	 * @return List
	 */
	List<OrgContactVO> findByCustomerIds(List<Long> customerIds);

	/**
	 * 下载导入客户联系人excel 模板
	 * @param response
	 * @param key
	 */
    void downloadTemplate(HttpServletResponse response, String key);

	/**
	 * 导入联系人 成功失败信息都放到redis
	 * @param file 联系人文件
	 * @return rediskey 缓存key
	 */
    R importLinkmanFile(MultipartFile file);

	/**
	 * 根据rediskey 获取导入的结果数据
	 * @param redisKey rediskey
	 * @param isSuccess true 成功数据 false 失败数据
	 * @param query 分页条件
	 * @return
	 */
    R getImportData(String redisKey, Boolean isSuccess, Query query);

	/**
	 * 保存验证通过的数据
	 * @param redisKey
	 * @return
	 */
    R submitpassdata(String redisKey);

	/**
	 * 根据客户名称精准查询联系人信息
	 * @param custManager 客户经理名称
	 * @return OrgContact
	 */
	OrgContact selectOneByCustManager(String custManager);

	/**
	 * 逻辑删除联系人并判断相关引用
	 * @param id 删除联系人主键Id
	 * @return 提示信息
	 */
	R deleteByContactId(String id);

	/**
	 * 根据客户id查询联系人信息
	 * @param id 客户Id
	 * @return OrgContact
	 */
	List<OrgContact> findByCustomerId(String id);
}
