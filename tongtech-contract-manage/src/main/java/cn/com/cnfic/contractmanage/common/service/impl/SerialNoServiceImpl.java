package cn.com.cnfic.contractmanage.common.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.cnfic.contractmanage.common.entity.SerialNo;
import cn.com.cnfic.contractmanage.common.mapper.SerialNoMapper;
import cn.com.cnfic.contractmanage.common.service.ISerialNoService;

@Service
public class SerialNoServiceImpl extends BaseServiceImpl<SerialNoMapper, SerialNo> implements ISerialNoService{

	@Override
	public String getSerialNo(String code, String prefix, int noLong) {
		QueryWrapper<SerialNo> queryWrapper = Condition.getQueryWrapper(new SerialNo());
		//获取当前年月日的字符串
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate currDate = LocalDate.now();
		String currDateString = df.format(currDate);
		//根据code查询之前是否生成过序列号
		queryWrapper.eq("code", code);
		SerialNo entity = this.getOne(queryWrapper);
		int no = 0;
		//如果没有生成过序列号，新增
		if(entity==null) {
			no=1;
			entity = new SerialNo();
			entity.setCode(code);
			entity.setNoDate(currDate);
			entity.setNo(no);
			this.save(entity);
		}else {//如果生成过序列号，但是日期不是当日，则修改为当日
			if(entity.getNoDate()!=null&&currDate.equals(entity.getNoDate())) {
				no = entity.getNo()+1;
			}else {
				no = 1;
			}
			entity.setNoDate(currDate);
			entity.setNo(no);
			this.updateById(entity);
		}
		return prefix+currDateString+codeAddOne(no, noLong);
	}
	
	private String codeAddOne(Integer no, int len) {
		String strHao = no.toString();
		while (strHao.length() < len) {
			strHao = "0" + strHao;
		}
		return strHao;
	}

}
