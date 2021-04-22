package cn.com.cnfic.contractmanage.contract.controller;

import cn.com.cnfic.contractmanage.contract.dto.ContractDTO;
import cn.com.cnfic.contractmanage.contract.entity.Contract;
import cn.com.cnfic.contractmanage.contract.service.IContractService;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.contract.wrapper.ContractWrapper;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 合约管理控制类
 *
 * @author zhaijw
 * @since 2021-03-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/contract")
@Api(value = "合约管理", tags = "合约管理接口")
public class ContractController extends BladeController {

    private IContractService contractService;

    /**
     * 合约管理分页查询
     *
     * @param contractDto 封装的合约dto类
     * @param query       分页查询信息
     * @return 包含分页信息的合约管理列表数据
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入contract")
    public R<IPage<ContractVO>> page(ContractDTO contractDto, Query query) {
        IPage<ContractVO> pages = contractService.selectContractPage(query, contractDto);
        return R.data(ContractWrapper.build().pageVO(pages));
}

    /**
     * 新增合约-基本信息
     */
    @PostMapping("/saveBasicInfo")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增合约-基本信息", notes = "传入contractDTO")
    public R<Contract> saveBasicInfo(@Valid @RequestBody ContractDTO contractDto) {
        return contractService.saveBasicInfo(contractDto);
    }

    /**
     * 新增合约-关联用户协议
     */
    @PostMapping("/saveUserAgreementInfo")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "新增合约-关联用户协议", notes = "传入contractDTO")
    public R saveUserAgreementInfo(@Valid @RequestBody ContractDTO contractDto) {
        return contractService.saveUserAgreementInfo(contractDto);
    }

    /**
     * 删除合约
     */
    @PostMapping("/removeContract")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "删除合约", notes = "传入contractDTO")
    public R removeContract(@Valid @RequestBody ContractDTO contractDto) {
        return contractService.removeContract(contractDto);
    }

    @GetMapping("/export")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "合约导出")
    public void exportOrgUserList(HttpServletResponse response, ContractDTO contract) {
        contractService.exportContractList(response, contract);
        return ;
    }
}
