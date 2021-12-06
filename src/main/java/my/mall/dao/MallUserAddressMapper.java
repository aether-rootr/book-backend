package my.mall.dao;

import my.mall.entity.MallUserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallUserAddressMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(MallUserAddress record);

    int insertSelective(MallUserAddress record);

    MallUserAddress selectByPrimaryKey(Long addressId);

    MallUserAddress getMyDefaultAddress(Long userId);

    List<MallUserAddress> findMyAddressList(Long userId);

    int updateByPrimaryKeySelective(MallUserAddress record);

    int updateByPrimaryKey(MallUserAddress record);
}
