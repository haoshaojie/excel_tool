/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.cnfic.contractmanage.goods.mapper;

import cn.com.cnfic.contractmanage.goods.dto.InfoGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.InfoGoods;
import cn.com.cnfic.contractmanage.goods.vo.InfoGoodsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 资讯商品表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public interface InfoGoodsMapper extends BaseMapper<InfoGoods> {

    /**
     * 自定义分页联合资讯和研报查询
     *
     * @param page
     * @param infoGoods
     * @return
     */
    List<InfoGoods> selectUnionGoodsPage(IPage page, InfoGoodsDTO infoGoods);

    /**
     * 导出数据
     *
     * @param infoGoods
     * @return
     */
    List<InfoGoodsVO> selectInfoGoodsExport(InfoGoodsDTO infoGoods);
}
