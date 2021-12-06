package my.mall.dao;

import my.mall.entity.MallOrderAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface MallOrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(MallOrderAddress record);

    int insertSelective(MallOrderAddress record);

    MallOrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(MallOrderAddress record);

    int updateByPrimaryKey(MallOrderAddress record);
}
