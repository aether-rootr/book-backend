package my.mall.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import my.mall.api.mall.vo.MallIndexCategoryVO;
import my.mall.common.MallException;
import my.mall.common.ServiceResultEnum;
import my.mall.service.MallCategoryService;
import my.mall.util.Result;
import my.mall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "mallUser", tags = "图书商城分类页面接口")
@RequestMapping("/api/mallUser")
public class MallGoodsCategoryAPI {

    @Resource
    private MallCategoryService mallCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<MallIndexCategoryVO>> getCategories() {
        List<MallIndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        System.out.println(categories);
        if (CollectionUtils.isEmpty(categories)) {
            MallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
