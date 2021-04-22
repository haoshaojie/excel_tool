package cn.com.cnfic.contractmanage.user.feign;

import cn.com.cnfic.contractmanage.user.service.IPersonalUserService;
import cn.com.cnfic.contractmanage.user.dto.PersonalUserDTO;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szy
 */
@RestController
@AllArgsConstructor
public class PersonalUserClient implements IPersonalUserClient{

    private IPersonalUserService personalUserService;
    private IPersonalUserService iPersonalUserService;

    @Override
    @PostMapping(ADD)
    public R<Boolean> addPersonalUser(PersonalUserDTO user) {
        return iPersonalUserService.savePersonalUser(user);
    }

    @Override
    @PostMapping(UPDATE)
    public R<Boolean> updatePersonalUser(PersonalUserDTO user) {
        return iPersonalUserService.updatePersonalUser(user);
    }

    @Override
    @GetMapping(EXIST_INVITE_CODE)
    public R<Boolean> existInviteCode(String code, Long accountId) {
        return personalUserService.existInviteCode(code, accountId);
    }

}
