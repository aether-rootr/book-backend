package my.mall.dao;

import my.mall.entity.User;
import my.mall.entity.UserRole;
import my.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据操作类
 */
@Repository
public interface MallUserMapper {
    /**
     * 通过id删除用户
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 向用户表中插入新数据
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 向用户表中选择插入数据
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 通过id查询用户
      * @param userId
     * @return
     */
    User selectByPrimaryKey(Long userId);

    /**
     * 通过用户名查询用户
     * @param loginName
     * @return
     */
    User selectByLoginName(String loginName);

    /**
     * 通过用户名查询用户角色
     * @param loginName
     * @return
     */
    UserRole findUserRoleByUsername(String loginName);

    /**
     * 通过用户名与密码查询用户
     * @param loginName
     * @param password
     * @return
     */
    User selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    /**
     * 选择修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);

    /**
     * 分页查询所用用户数据
     * @param pageUtil
     * @return
     */
    List<User> findMallUserList(PageQueryUtil pageUtil);

    /**
     * 查询用户总数
     * @param pageUtil
     * @return
     */
    int getTotalMallUsers(PageQueryUtil pageUtil);

    /**
     * 用户锁定
     * @param ids
     * @param lockStatus
     * @return
     */
    int lockUserBatch(@Param("ids") Long[] ids, @Param("lockStatus") int lockStatus);
}
