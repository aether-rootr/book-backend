package my.mall.api.mall;

import io.swagger.annotations.*;
import my.mall.api.mall.vo.MallSearchGoodsVO;
import my.mall.common.Constants;
import my.mall.common.MallException;
import my.mall.common.ServiceResultEnum;
import my.mall.config.annotation.TokenToMallUser;
import my.mall.api.mall.vo.MallGoodsDetailVO;
import my.mall.entity.MallGoods;
import my.mall.entity.User;
import my.mall.service.MallGoodsService;
import my.mall.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "mallUser", tags = "图书商城商品相关接口")
@RequestMapping("/api/mallUser")
public class MallGoodsAPI {

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsAPI.class);

    @Resource
    private MallGoodsService mallGoodsService;

    @GetMapping("/search")
    @ApiOperation(value = "商品搜索接口", notes = "根据关键字和分类id进行搜索")
    public Result<PageResult<List<MallSearchGoodsVO>>> search(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String keyword,
                                                              @RequestParam(required = false) @ApiParam(value = "分类id") Long goodsCategoryId,
                                                              @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
                                                              @RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                                                              @TokenToMallUser User loginUser) {

        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginUser.getUserId());

        Map params = new HashMap(8);
        //两个搜索参数都为空，直接返回异常
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            MallException.fail("非法的搜索参数");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //对keyword做过滤 去掉空格
        if (!StringUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            params.put("orderBy", orderBy);
        }
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallGoodsService.searchMallGoods(pageUtil));
    }

    @GetMapping("/goods/detail/{goodsId}")
    @ApiOperation(value = "商品详情接口", notes = "传参为商品id")
    public Result<MallGoodsDetailVO> goodsDetail(@ApiParam(value = "商品id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser User loginUser) {
        logger.info("goods detail api,goodsId={},userId={}", goodsId, loginUser.getUserId());
        if (goodsId < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        MallGoods goods = mallGoodsService.getMallGoodsById(goodsId);
        if (goods == null) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            MallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        MallGoodsDetailVO goodsDetailVO = new MallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        return ResultGenerator.genSuccessResult(goodsDetailVO);
    }

}
