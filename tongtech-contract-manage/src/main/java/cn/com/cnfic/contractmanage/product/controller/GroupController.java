package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.dto.GroupDTO;
import cn.com.cnfic.contractmanage.product.entity.Group;
import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.service.IGroupDetailService;
import cn.com.cnfic.contractmanage.product.service.IGroupService;
import cn.com.cnfic.contractmanage.product.vo.GroupVO;
import cn.com.cnfic.contractmanage.product.wrapper.GroupDetailWrapper;
import cn.com.cnfic.contractmanage.product.wrapper.GroupWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @desc 组合产品表 控制器
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/group")
@Api(value = "组合产品表", tags = "组合产品表接口")
public class GroupController extends BladeController {

	private IGroupService groupService;

	private IGroupDetailService groupDetailService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入group")
	public R<GroupVO> detail(Group group) {
		Group detail = groupService.getOne(Condition.getQueryWrapper(group));
		GroupVO vo = GroupWrapper.build().entityVO(detail);
		List<GroupDetail> details = groupDetailService.getDetailsById(group.getId());
		vo.setDetailList(GroupDetailWrapper.build().listVO(details));
		return R.data(vo);
	}

	/**
	* 分页 组合产品表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入group")
	public R<IPage<GroupVO>> list(GroupDTO group, Query query) {
		IPage<Group> pages = groupService.selectGroupPage(query, group);
		return R.data(GroupWrapper.build().pageVO(pages));
	}
	/**
	* 修改 组合产品表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入group")
	public R update(@Valid @RequestBody GroupDTO group) {
		return groupService.updateGroup(group);
	}

	/**
	* 新增或修改 组合产品表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入group")
	public R submit(@Valid @RequestBody GroupDTO group) {
		return groupService.saveGroup(group);
	}

	
	/**
	* 删除 组合产品表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return groupService.removeGroupByIds(ids);
	}

	/**
	 * 修改状态
	 * @param ids
	 * @param state
	 * @return
	 */
	@PostMapping("/updateState")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "产品上下架", notes = "传入ids,state")
	public R updateState(@ApiParam(value = "主键集合", required = true) @RequestParam String ids,
						 @ApiParam(value = "产品状态", required = true) @RequestParam Integer state){
		return groupService.updateGroupState(ids, state);
	}

	/**
	 * 校验组合产品
	 * @return
	 */
	@PostMapping("/validProdName")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "校验组合产品", notes = "校验组合产品")
	public R validProdName(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "name", required = true) String name){
		return groupService.validProdName(id,name);
	}

}
