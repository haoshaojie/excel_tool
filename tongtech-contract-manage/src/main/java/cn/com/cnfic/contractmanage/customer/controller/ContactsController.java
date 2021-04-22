package cn.com.cnfic.contractmanage.customer.controller;

import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @desc 联系人相关Api
 * @auther yangchuan
 * @date 2021/3/26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/linkman")
@Api(value = "联系人", tags = "企业客户联系人表接口")
public class ContactsController {
    private IOrgContactService contactService;
    /**
     *	下载客户联系人模板
     //	 * @param orgCustomer
     * @param response
     */
    @GetMapping("/downloadContractTemplate")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "下载客户联系人模板及导出失败数据")
    public void downloadTemplate(HttpServletResponse response, @RequestParam(required = false) String key) {
        contactService.downloadTemplate(response,key);
    }

    @PostMapping("/importLinkmanFile")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "导入联系人文件")
    public R importBlackWhiteList(MultipartFile file) {
        return contactService.importLinkmanFile(file);
    }
    @GetMapping("/getImportData")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "获取导入的结果数据")
    public R getImportData(@RequestParam(value = "redisKey") String redisKey, @RequestParam(value = "isSuccess")Boolean isSuccess, Query query) {
        return contactService.getImportData(redisKey,isSuccess,query);
    }
    @GetMapping("/submitpassdata")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "提交客户信息数据")
    public R submitpassdata(@RequestParam(value = "redisKey") String redisKey) {
        return contactService.submitpassdata(redisKey);
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "查询联系人", notes = "传入contact")
    public R<List<OrgContact>> detail(OrgContact contact) {
        contact.setIsDeleted(0);
        List<OrgContact> detail = contactService.list(Condition.getQueryWrapper(contact));
        return R.data(detail);
    }

    @GetMapping("/getContractById")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "根据Id查询联系人", notes = "传入contact")
    public R<OrgContact> getContractById(String id) {
        OrgContact detail = contactService.getById(id);
        return R.data(detail);
    }
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "逻辑删除", notes = "传入id")
    public R remove(@ApiParam(value = "主键", required = true) @RequestParam String id) {
        return contactService.deleteByContactId(id);
    }
    @GetMapping("/findAllByCustId")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "根据客户Id查询联系人列表", notes = "传入custId")
    public R<List<OrgContact>> findAllByCustId(String id) {
        List<OrgContact> result = contactService.list(Wrappers.<OrgContact>query().lambda().eq(OrgContact::getCustId,id).eq(OrgContact::getIsDeleted,0));
        return R.data(result);
    }
}
