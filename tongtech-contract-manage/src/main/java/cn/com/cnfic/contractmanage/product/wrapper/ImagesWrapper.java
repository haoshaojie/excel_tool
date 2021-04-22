package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.entity.Images;
import cn.com.cnfic.contractmanage.product.vo.ImagesVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品项图片表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public class ImagesWrapper extends BaseEntityWrapper<Images, ImagesVO>  {

    public static ImagesWrapper build() {
        return new ImagesWrapper();
    }

	@Override
	public ImagesVO entityVO(Images images) {
		ImagesVO imagesVO = BeanUtil.copy(images, ImagesVO.class);

		return imagesVO;
	}

}
