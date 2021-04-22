package cn.com.cnfic.contractmanage.user.service;

import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.vo.OrgUserExcelVO;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @desc 企业用户表 服务类
 * @author Cnfic-UserManage
 * @date 2021-03-29
 */
public interface IOrgUserService extends BaseService<OrgUser> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgUser
	 * @return
	 */
	IPage<OrgUserVO> selectOrgUserPage(IPage<OrgUserVO> page, OrgUserVO orgUser);

	/**
	 * 查询用户详情
	 * @param queryWrapper 查询参数
	 * @return vo
	 */
    OrgUserVO getDetail(QueryWrapper<OrgUser> queryWrapper);

	/**
	 * 新增或者修改用户信息
	 * @param orgUser
	 * @return
	 */
    Boolean saveOrUpdateUser(OrgUser orgUser);

	/**
	 * 下载导入用户模板
	 * @param response
	 * @param key redis 数据key 如果传入key就是下载对比失败文件
	 */
    void downloadTemplate(HttpServletResponse response, String key);

	/**
	 * 导入用户模板文件
	 * @param file
	 * @return  redis缓存key
	 */
    R imporCustomerFile(MultipartFile file);

	/**
	 * 通过rediskey 获取导入数据
	 * @param redisKey key
	 * @param isSuccess false 失败数据 true 成功数据
	 * @param query 分页条件
	 * @return 返回封装结果数据
	 */
    R getImportData(String redisKey, Boolean isSuccess, Query query);

	/**
	 * 提交缓存用户信息数据
	 * @param redisKey  redisKey
	 * @return
	 */
	R submitpassdata(String redisKey);

	/**
	 * 导出机构用户
	 * @param response
	 * @param orgUser orgUser 导出列信息及查询条件
	 */
    void exportOrgUserList(HttpServletResponse response, OrgUserVO orgUser);

	/**
	 * 根据客户经理Id查询用户
	 * @param id 客户经理Id
	 * @return OrgUser
	 */
	OrgUser findOneOrgUserByManager(String id);

	/**
	 * 通过客户id集合查询用户信息
	 * @param customerIds 客户id集合
	 * @return List<OrgUserExcelVO>
	 */
    List<OrgUserExcelVO> findByCustomerIds(List<Long> customerIds);
}
