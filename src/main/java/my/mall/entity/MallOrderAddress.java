package my.mall.entity;

import lombok.Data;

/**
 * 用户订单地址类
 */
@Data
public class MallOrderAddress {
    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
