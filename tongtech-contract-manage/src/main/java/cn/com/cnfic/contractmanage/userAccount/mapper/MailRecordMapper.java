package cn.com.cnfic.contractmanage.userAccount.mapper;


import cn.com.cnfic.contractmanage.userAccount.entity.MailRecord;
import cn.com.cnfic.contractmanage.userAccount.vo.MailRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 邮件发送记录表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface MailRecordMapper extends BaseMapper<MailRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param mailRecord
	 * @return
	 */
	List<MailRecordVO> selectMailRecordPage(IPage page, MailRecordVO mailRecord);

}
