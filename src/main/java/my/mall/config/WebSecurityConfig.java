package my.mall.config;

import my.mall.config.handler.RestAuthAccessDeniedHandler;
import my.mall.security.AuthencationTokenFilter;
import my.mall.service.MyUserDetailsService;
import my.mall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
//开启@secured 注解过滤权限 使用表达式时间方法级别的安全性
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private RestAuthAccessDeniedHandler accessDeniedHandler;

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //任何请求都会被认证，所有请求都会被拦截
        http.authorizeRequests()
                //放行不需要拦截的请求
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/api/mallUser/**").permitAll()
                .antMatchers("/manage-api/admin/**").permitAll()
                .antMatchers("/manage-api/admin/root/**").hasRole("ROOT")
                .and()
                .authorizeRequests()			// 对请求进行授权
                .anyRequest()					// 任意的请求
                .authenticated();				// 都需要登录之后才能访问

        //关闭跨域攻击
        http.cors().and().csrf().disable();
        //添加登录授权过滤器
        http.addFilterBefore(AuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         // 无权访问 JSON 格式的数据
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }


    @Bean
    public AuthencationTokenFilter AuthencationTokenFilter(){
        return new AuthencationTokenFilter();
    }

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * 设置用户密码的加密方式为MD5加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5Util();
    }

     /**
      * 自定义UserDetailsService，从数据库中读取用户信息
      * @return
      */
    @Bean
    public UserDetailsService myUserDetailsService(){
        return new MyUserDetailsService();
    }

    /**
     * 取消ROLE前缀
     * @return
     */
//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults() {
//         // Remove the ROLE_ prefix
//        return new GrantedAuthorityDefaults("");
//    }

}
