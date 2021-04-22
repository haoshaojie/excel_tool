package cn.com.cnfic.contractmanage.userAccount.mapper;

import cn.com.cnfic.contractmanage.userAccount.entity.MailProductUrl;
import cn.com.cnfic.contractmanage.userAccount.vo.MailProductUrlVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 邮件模板产品下载链接表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface MailProductUrlMapper extends BaseMapper<MailProductUrl> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param mailProductUrl
	 * @return
	 */
	List<MailProductUrlVO> selectMailProductUrlPage(IPage page, MailProductUrlVO mailProductUrl);

}
