package my.mall.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import my.mall.api.mall.param.SaveCartItemParam;
import my.mall.api.mall.param.UpdateCartItemParam;
import my.mall.common.Constants;
import my.mall.common.MallException;
import my.mall.common.ServiceResultEnum;
import my.mall.config.annotation.TokenToMallUser;
import my.mall.api.mall.vo.MallShoppingCartItemVO;
import my.mall.entity.User;
import my.mall.entity.MallShoppingCartItem;
import my.mall.service.MallShoppingCartService;
import my.mall.util.PageQueryUtil;
import my.mall.util.PageResult;
import my.mall.util.Result;
import my.mall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "mallUser", tags = "5.图书商城购物车相关接口")
@RequestMapping("/api/mallUser")
public class MallShoppingCartAPI {

    @Resource
    private MallShoppingCartService mallShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<MallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser User loginUser) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<MallShoppingCartItemVO>> cartItemList(@TokenToMallUser User loginUser) {
        return ResultGenerator.genSuccessResult(mallShoppingCartService.getMyShoppingCartItems(loginUser.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser User loginUser) {
        String saveResult = mallShoppingCartService.saveMallCartItem(saveCartItemParam, loginUser.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser User loginUser) {
        String updateResult = mallShoppingCartService.updateMallCartItem(updateCartItemParam, loginUser.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   @TokenToMallUser User loginUser) {
        MallShoppingCartItem newBeeMallCartItemById = mallShoppingCartService.getMallCartItemById(newBeeMallShoppingCartItemId);
        if (!loginUser.getUserId().equals(newBeeMallCartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = mallShoppingCartService.deleteById(newBeeMallShoppingCartItemId, loginUser.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<MallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser User loginUser) {
        if (cartItemIds.length < 1) {
            MallException.fail("参数异常");
        }
        int priceTotal = 0;
        List<MallShoppingCartItemVO> itemsForSettle = mallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            MallException.fail("参数异常");
        } else {
            //总价
            for (MallShoppingCartItemVO mallShoppingCartItemVO : itemsForSettle) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                MallException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
