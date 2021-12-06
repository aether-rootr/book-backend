package my.mall.service;

import my.mall.entity.MallGoods;
import my.mall.util.PageQueryUtil;
import my.mall.util.PageResult;

import java.util.List;

public interface MallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveMallGoods(MallGoods goods);


    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateMallGoods(MallGoods goods);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    MallGoods getMallGoodsById(Long id);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchMallGoods(PageQueryUtil pageUtil);
}
