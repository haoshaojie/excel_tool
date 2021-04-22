package cn.com.cnfic.contractmanage.user.service.impl;

import cn.com.cnfic.contractmanage.product.enums.MsgEnum;
import cn.com.cnfic.contractmanage.user.dto.PersonalUserDTO;
import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.excel.UserExcelExportConstans;
import cn.com.cnfic.contractmanage.user.mapper.PersonalUserMapper;
import cn.com.cnfic.contractmanage.user.service.IPersonalUserService;
import cn.com.cnfic.contractmanage.user.vo.PersonalUserVO;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.feign.IUserClient;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * @author Cnfic-UserManage
 * @desc 个人用户表 服务实现类
 * @date 2021-04-01
 */
@Service
@AllArgsConstructor
public class PersonalUserServiceImpl extends BaseServiceImpl<PersonalUserMapper, PersonalUser> implements IPersonalUserService {

    private IUserClient userClient;

    @Override
    public IPage<PersonalUserVO> selectPersonalUserPage(IPage<PersonalUserVO> page, PersonalUserVO personalUser) {
        return page.setRecords(baseMapper.selectPersonalUserPage(page, personalUser));
    }

    @Override
    @SneakyThrows
    public void exportPersonalUser(HttpServletResponse response, PersonalUserVO personalUser) {
        List<PersonalUserVO> personalUserVOS = baseMapper.selectPersonalUserPage(personalUser);
        //处理更新人
        List<Long> userIds = personalUserVOS.stream().map(BaseEntity::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userMap = userClient.listUserByIds(StringUtils.join(userIds, ",")).stream().collect(toMap(User::getId, User::getName, (key1, key2) -> key2));
        personalUserVOS.stream().forEach(e -> {
            e.setUpdateUserName(userMap.get(e.getUpdateUser()));
        });
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileNameEncode = URLEncoder.encode(UserExcelExportConstans.EXPORT_PERSONAL_USER_FILE_NAME, StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            ;
            WriteSheet customerSheet = EasyExcel.writerSheet(UserExcelExportConstans.EXPORT_CUSTOMER_SHEET1_NAME).head(PersonalUserVO.class).includeColumnFiledNames(Arrays.asList(UserExcelExportConstans.EXPORT_PERSONAL_USER_COLMUN.split(","))).build();
            excelWriter.write(personalUserVOS, customerSheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @Override
    public R<Boolean> savePersonalUser(PersonalUserDTO personalUser) {
        // 校验 账户id 不能为空 以及该账户下只能存在一个用户
        Assert.isTrue(Func.isNotEmpty(personalUser.getAccountId()), "账户ID 不能为空!");
        QueryWrapper<PersonalUser> queryWrapper = Condition.getQueryWrapper(new PersonalUser());
        queryWrapper.eq("account_id", personalUser.getAccountId());
        Integer t = baseMapper.selectCount(queryWrapper);
        Assert.isTrue(t <= 0, "用户新增失败，该账户只能生成一个用户!");
        if (Func.isEmpty(personalUser.getCreateTime())) {
            personalUser.setCreateTime(now());
        }
        if (Func.isEmpty(personalUser.getRegisterTime())) {
            personalUser.setRegisterTime(LocalDateTime.now().format(DateUtil.DATE_FORMATTER));
        }
        if (Func.isEmpty(personalUser.getCreateUser())) {
            Long userId = SecureUtil.getUserId();
            personalUser.setCreateUser(userId);
        }
        personalUser.setIsDeleted(0);
        int i = baseMapper.insert(personalUser);
        return R.data(i > 0, i > 0 ? "操作成功!" : "操作失败");
    }

    @Override
    public R<Boolean> updatePersonalUser(PersonalUserDTO personalUser) {
        // 校验 账户id 不能为空
        Assert.isTrue(Func.isNotEmpty(personalUser.getAccountId()), "账户ID 不能为空!");
        if (Func.isEmpty(personalUser.getCreateTime())) {
            personalUser.setUpdateTime(now());
        }
        if (Func.isEmpty(personalUser.getCreateUser())) {
            Long userId = SecureUtil.getUserId();
            personalUser.setUpdateUser(userId);
        }
        // 根据账户ID修改
        int i = baseMapper.updatePersonalUserByAccount(personalUser);
        return R.data(i > 0, i > 0 ? "操作成功!" : "操作失败");
    }

    @Override
    public R<Boolean> existInviteCode(String code, Long accountId) {
        QueryWrapper<PersonalUser> query = Condition.getQueryWrapper(new PersonalUser());
        if (Func.isNotEmpty(accountId)) {
            query.ne("account_id", accountId);
        }
        query.eq("invite_code", code);
        return R.data(this.count(query) == 0,
                this.count(query) != 0 ? MsgEnum.SUCCESS_MSG.getMsg() : MsgEnum.CHECK_USER_INVITE_CODE_ISUSED_MSG.getMsg());
    }
}
