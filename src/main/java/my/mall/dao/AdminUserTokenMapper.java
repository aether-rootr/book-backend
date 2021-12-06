package my.mall.dao;

import my.mall.entity.AdminUserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserTokenMapper {

    /**
     * 根据Id删除
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 新增管理员
     * @param record
     * @return
     */
    int insert(AdminUserToken record);

    /**
     * 选择新增管理信息
     * @param record
     * @return
     */
    int insertSelective(AdminUserToken record);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    AdminUserToken selectByPrimaryKey(Long userId);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    AdminUserToken selectByToken(String token);

    /**
     * 根据关键字修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AdminUserToken record);

    /**
     * 修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(AdminUserToken record);
}
