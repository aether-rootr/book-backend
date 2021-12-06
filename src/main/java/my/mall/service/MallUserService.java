package my.mall.service;

import my.mall.api.mall.param.MallUserUpdateParam;
import my.mall.entity.User;
import my.mall.entity.UserRole;
import my.mall.util.PageQueryUtil;
import my.mall.util.PageResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MallUserService {

    /**
     * 用户注册
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password,String NickName);


    /**
     * 登录
     *
     * @param loginName
     * @param passwordMD5
     * @return
     */
    String login(String loginName, String passwordMD5);

    /**
     * 查询用户信息
     *
     * @param username
     * @return
     */
    UserRole findUserRoleByUsername(String username);

    /**
     * 用户信息修改
     *
     * @param mallUser
     * @return
     */
    Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId);

    /**
     * 登出接口
     * @param userId
     * @return
     */
    Boolean logout(Long userId);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Long[] ids, int lockStatus);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallUsersPage(PageQueryUtil pageUtil);
}
