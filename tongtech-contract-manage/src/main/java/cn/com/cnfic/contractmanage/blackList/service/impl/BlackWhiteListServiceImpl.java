package cn.com.cnfic.contractmanage.blackList.service.impl;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.BlackWhiteListExport;
import cn.com.cnfic.contractmanage.blackList.mapper.BlackWhiteListMapper;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import cn.com.cnfic.contractmanage.blackList.wrapper.BlackWhiteListWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 黑白名单表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
@AllArgsConstructor
@Service
public class BlackWhiteListServiceImpl extends ServiceImpl<BlackWhiteListMapper, BlackWhiteList> implements IBlackWhiteListService {

    /**
     * 黑白名单交易所校验错误返回信息
     * {@value}
     */
    private static final String EXCHANGE_TYPE_ERROR_MESSAGE = "一个交易所下的同一个数据编码只能存在一个白名单或黑名单，不能黑白名单均有";
    /**
     * 黑白名单交易所数据重复校验错误返回信息
     * {@value}
     */
    private static final String CUSTOMER_CODE_ERROR_MESSAGE = "新增的交易所-数据编码-名单类型-客户编码在系统中已存在";
    /**
     * 数据不存在校验错误信息返回
     * {@value}
     */
    private static final String NOT_EXIST_ERROR_MESSAGE = "数据不存在";

    @Override
    public List<BlackWhiteListExport> selectExport(BlackWhiteList blackWhiteList) {
        return baseMapper.selectExport(this.blackWhiteListLambdaQueryWrapper(blackWhiteList));
    }

    @Override
    public R<IPage<BlackWhiteList>> page(BlackWhiteList blackWhiteList, Query query) {
        return R.data(page(Condition.getPage(query), this.blackWhiteListLambdaQueryWrapper(blackWhiteList)));
    }

    /**
     * 构建黑白名单管理查询条件
     *
     * @param blackWhiteList
     * @return
     */
    private LambdaQueryWrapper<BlackWhiteList> blackWhiteListLambdaQueryWrapper(BlackWhiteList blackWhiteList) {
        //数据编码、交易所、客户名称、客户编码支持模糊查询
        LambdaQueryWrapper<BlackWhiteList> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtil.isNotBlank(blackWhiteList.getExchange())) {
            queryWrapper.like(BlackWhiteList::getExchange, blackWhiteList.getExchange());
        }
        if (StringUtil.isNotBlank(blackWhiteList.getDataCode())) {
            queryWrapper.like(BlackWhiteList::getDataCode, blackWhiteList.getDataCode());
        }
        if (StringUtil.isNotBlank(blackWhiteList.getCustName())) {
            queryWrapper.like(BlackWhiteList::getCustName, blackWhiteList.getCustName());
        }
        if (StringUtil.isNotBlank(blackWhiteList.getCustCode())) {
            queryWrapper.like(BlackWhiteList::getCustCode, blackWhiteList.getCustCode());
        }
        if (Objects.nonNull(blackWhiteList.getType())) {
            queryWrapper.eq(BlackWhiteList::getType, blackWhiteList.getType());
        }
        if (StringUtil.isNotBlank(blackWhiteList.getAuditState())) {
            queryWrapper.eq(BlackWhiteList::getAuditState, blackWhiteList.getAuditState());
        }
        queryWrapper.eq(BlackWhiteList::getIsDeleted, NumberUtils.INTEGER_ZERO);
        return queryWrapper;
    }

    @Override
    public R<BlackWhiteListVO> getBlackWhiteList(BlackWhiteList blackWhiteList) {
        BlackWhiteList detail = getOne(Condition.getQueryWrapper(blackWhiteList));
        if (Objects.nonNull(detail)) {
            return R.data(BlackWhiteListWrapper.build().entityVO(detail));
        }
        return R.fail(NOT_EXIST_ERROR_MESSAGE);
    }

    @Override
    public R submit(BlackWhiteList blackWhiteList) {
        String result = checkBlackWhiteListParams(blackWhiteList);
        if (StringUtil.isNotBlank(result)) {
            return R.fail(result);
        }
        result = initBlackWhiteListData(blackWhiteList);
        if (StringUtil.isNotBlank(result)) {
            return R.fail(result);
        }
        return R.status(saveOrUpdate(blackWhiteList));
    }

    @Override
    public String initBlackWhiteListData(BlackWhiteList blackWhiteList) {
        if (Objects.nonNull(blackWhiteList.getId())) {
            BlackWhiteList data = baseMapper.selectById(blackWhiteList.getId());
            if (Objects.nonNull(data)) {
                //修改的初始化参数，单纯的备注项变更不需要重置审核信息
                blackWhiteList.setUpdateTime(DateUtil.now());
                blackWhiteList.setUpdateUser(SecureUtil.getUserId());
                if (!BooleanUtils.and(ArrayUtils
                        .toArray(StringUtil.equals(blackWhiteList.getExchange(), data.getExchange()),
                                StringUtil.equals(blackWhiteList.getDataCode(), data.getDataCode()),
                                blackWhiteList.getType().equals(data.getType()),
                                StringUtil.equals(blackWhiteList.getCustName(), data.getCustName()),
                                StringUtil.equals(blackWhiteList.getCustCode(), data.getCustCode())))) {
                    resetAudit(blackWhiteList);
                }
            } else {
                return NOT_EXIST_ERROR_MESSAGE;
            }
        } else {
            //新增的初始化参数
            blackWhiteList.setCreateTime(DateUtil.now());
            blackWhiteList.setCreateUser(SecureUtil.getUserId());
            resetAudit(blackWhiteList);
        }
        //在新增或者编辑了除备注信息外的其他信息均要重置审核状态
        blackWhiteList.setSource(NumberUtils.INTEGER_ONE);
        blackWhiteList.setIsDeleted(NumberUtils.INTEGER_ZERO);
        return StringUtils.EMPTY;
    }

    /**
     * 重置黑白名单审核信息
     *
     * @param blackWhiteList
     */
    private void resetAudit(BlackWhiteList blackWhiteList) {
        blackWhiteList.setAuditUser(null);
        blackWhiteList.setAuditState(BlackWhiteListConstant.DEFAULT_AUDIT_VALUE);
        blackWhiteList.setAuditTime(null);
        blackWhiteList.setAuditResult(null);
    }

    @Override
    public String checkBlackWhiteListParams(BlackWhiteList blackWhiteList) {
        //1.一个交易所下的同一个数据编码只能存在一个白名单或黑名单，不能黑白名单均有
        LambdaQueryWrapper<BlackWhiteList> exchangeCheckWrapper = Wrappers.lambdaQuery(BlackWhiteList.class)
                .eq(BlackWhiteList::getExchange, blackWhiteList.getExchange())
                .eq(BlackWhiteList::getDataCode, blackWhiteList.getDataCode());
        List<Object> types = baseMapper.selectObjs(exchangeCheckWrapper.select(BlackWhiteList::getType));
        types.add(blackWhiteList.getType());
        if (Sets.newHashSet(types).size() > 1) {
            return EXCHANGE_TYPE_ERROR_MESSAGE;
        }
        //2.新增的交易所-数据编码-名单类型-客户编码在系统是否已存在，存在则无法再次新增
        if (StringUtil.isNotBlank(blackWhiteList.getCustCode())) {
            LambdaQueryWrapper<BlackWhiteList> allCheckWrapper = exchangeCheckWrapper
                    .eq(BlackWhiteList::getCustCode, blackWhiteList.getCustCode());
            List<BlackWhiteList> custChecks = baseMapper.selectList(allCheckWrapper);
            if (custChecks.size() > 0) {
                return CUSTOMER_CODE_ERROR_MESSAGE;
            }
        }
        return StringUtils.EMPTY;
    }

}
