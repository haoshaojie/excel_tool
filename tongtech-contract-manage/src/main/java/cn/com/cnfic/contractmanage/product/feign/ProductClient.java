package cn.com.cnfic.contractmanage.product.feign;

import cn.com.cnfic.contractmanage.product.service.IProductService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szy
 */
@RestController
@AllArgsConstructor
public class ProductClient implements IProductClient{

    private IProductService iProductService;
    @Override
    @PostMapping(UPDATE_STATE_TASK)
    public R<Boolean> updateStateByTask(Integer state) {
        return iProductService.updateStateByTask(state);
    }
}
