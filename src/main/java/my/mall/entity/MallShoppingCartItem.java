package my.mall.entity;

import lombok.Data;

import java.util.Date;

/**
 * 购物表
 */
@Data
public class MallShoppingCartItem {
    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
