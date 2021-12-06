package my.mall.security;

import my.mall.common.MallException;
import my.mall.common.ServiceResultEnum;
import my.mall.config.handler.TokenToMallUserMethodArgumentResolver;
import my.mall.dao.AdminUserMapper;
import my.mall.dao.AdminUserTokenMapper;
import my.mall.dao.MallUserMapper;
import my.mall.dao.MallUserTokenMapper;
import my.mall.entity.AdminUserToken;
import my.mall.entity.MallUserToken;
import my.mall.service.MyUserDetailsService;
import my.mall.util.NumberUtil;
import my.mall.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 登录授权过滤器
 */
public class AuthencationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private TokenToMallUserMethodArgumentResolver TokenToMallUserMethodArgumentResolver;

    @Autowired
    private MyUserDetailsService userDetailsService;

    public my.mall.config.handler.TokenToMallUserMethodArgumentResolver getTokenToMallUserMethodArgumentResolver() {
        return TokenToMallUserMethodArgumentResolver;
    }

    public void setTokenToMallUserMethodArgumentResolver(my.mall.config.handler.TokenToMallUserMethodArgumentResolver tokenToMallUserMethodArgumentResolver) {
        TokenToMallUserMethodArgumentResolver = tokenToMallUserMethodArgumentResolver;
    }

    public MyUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public MallUserMapper getMallUserMapper() {
        return mallUserMapper;
    }

    public void setMallUserMapper(MallUserMapper mallUserMapper) {
        this.mallUserMapper = mallUserMapper;
    }

    public MallUserTokenMapper getMallUserTokenMapper() {
        return mallUserTokenMapper;
    }

    public void setMallUserTokenMapper(MallUserTokenMapper mallUserTokenMapper) {
        this.mallUserTokenMapper = mallUserTokenMapper;
    }

    public AdminUserMapper getAdminUserMapper() {
        return adminUserMapper;
    }

    public void setAdminUserMapper(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    public AdminUserTokenMapper getAdminUserTokenMapper() {
        return adminUserTokenMapper;
    }

    public void setAdminUserTokenMapper(AdminUserTokenMapper adminUserTokenMapper) {
        this.adminUserTokenMapper = adminUserTokenMapper;
    }

    @Autowired
    private MallUserMapper mallUserMapper;
    @Autowired
    private MallUserTokenMapper mallUserTokenMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private AdminUserTokenMapper adminUserTokenMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //拿到头部
        String token = httpServletRequest.getHeader("token");
        String http = httpServletRequest.getServletPath();
        http = http.substring(1,4);
        System.out.println(httpServletRequest.getRequestURL()+" 2 "+httpServletRequest.getMethod());
        if (http.equals("api") || http.equals("upl")){
            //存在token
            if (token != null && token.length() != 0){
                MallUserToken mallUserToken = mallUserTokenMapper.selectByToken(token);
                //过期或未登录
                if (mallUserToken == null || mallUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    Date now = new Date();
                    //过期时间 48 小时
                    Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
                    if (mallUserToken == null) {
                        mallUserToken.setToken(token);
                        mallUserToken.setUpdateTime(now);
                        mallUserToken.setExpireTime(expireTime);
                        //修改token数据
                        mallUserTokenMapper.updateByPrimaryKeySelective(mallUserToken);
                    }
                }
                String username = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId()).getLoginName();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //登录
                    System.out.println(username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //判断token是否有效,重新设置到用户对象
                    if (mallUserToken != null && mallUserToken.getExpireTime().getTime() > System.currentTimeMillis()){
                        UsernamePasswordAuthenticationToken authenticationToken = new
                                UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }
        else {
            String admintoken = httpServletRequest.getHeader("token");
            if (admintoken != null && admintoken.length() != 0 ){
                //管理
                AdminUserToken adminUserToken = adminUserTokenMapper.selectByToken(admintoken);
                //过期或未登录
                if (adminUserToken == null || adminUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    Date now = new Date();
                    //过期时间 48 小时
                    Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
                    if (adminUserToken == null) {
                        adminUserToken.setAdminUserId(adminUserToken.getAdminUserId());
                        adminUserToken.setToken(admintoken);
                        adminUserToken.setUpdateTime(now);
                        adminUserToken.setExpireTime(expireTime);
                        //修改token数据
                        adminUserTokenMapper.updateByPrimaryKeySelective(adminUserToken);
                    }
                }
                String username1 = adminUserMapper.selectByPrimaryKey(adminUserToken.getAdminUserId()).getLoginUserName();
                if (username1 != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //登录
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username1);
                    //判断token是否有效,重新设置到用户对象
                    if (adminUserToken != null && adminUserToken.getExpireTime().getTime() > System.currentTimeMillis()){
                        UsernamePasswordAuthenticationToken authenticationToken = new
                                UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
