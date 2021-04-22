package cn.com.cnfic.contractmanage.product.feign;

import cn.com.cnfic.contractmanage.product.service.IProductItemService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szy
 */
@RestController
@AllArgsConstructor
public class ProductItemClient implements IProductItemClient{

    private IProductItemService iProductItemService;
    @Override
    @PostMapping(ITEMSTATE)
    public R<Boolean> updateStateByTask(Integer itemState) {
        return iProductItemService.updateStateByTask(itemState);
    }
}
