package my.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页详情VO
 */
@Data
public class IndexInfoVO implements Serializable {


    @ApiModelProperty("首页热销商品(列表)")
    private List<MallIndexConfigGoodsVO> hotGoodses;

    @ApiModelProperty("首页新品推荐(列表)")
    private List<MallIndexConfigGoodsVO> newGoodses;

    @ApiModelProperty("首页推荐商品(列表)")
    private List<MallIndexConfigGoodsVO> recommendGoodses;
}
