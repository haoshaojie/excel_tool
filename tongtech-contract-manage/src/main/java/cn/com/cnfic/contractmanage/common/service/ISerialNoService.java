package cn.com.cnfic.contractmanage.common.service;

import org.springblade.core.mp.base.BaseService;

import cn.com.cnfic.contractmanage.common.entity.SerialNo;


/**
 * 业务序列号服务接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
public interface ISerialNoService extends BaseService<SerialNo>{
	
	/**
	 * 生成序列号(前缀+日期+序号)
	 * @param code 业务编码
	 * @param prefix 生成的序列号前缀
	 * @param noLong 序号的长度(不包括前缀和日期，前面补0)
	 * @return
	 */
	public String getSerialNo(String code,String prefix,int noLong);
}
