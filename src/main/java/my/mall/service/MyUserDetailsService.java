package my.mall.service;

import my.mall.entity.MyUserDetails;
import my.mall.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MallUserService mallUserService;

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRole userRole = mallUserService.findUserRoleByUsername(username);
        if (userRole == null){
             userRole = adminUserService.findadminRoleByUsername(username);
        }
        System.out.println(userRole.getRoleName());
        if(userRole.getRoleName() == null) {
            userRole.setRoleName("ROLE_USER");
        }
        System.out.println(userRole.getLoginName());
        List<SimpleGrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setUsername(userRole.getLoginName());
        userDetails.setPassword(userRole.getPasswordMd5());
        userDetails.setAuthorities(auth);
        return userDetails;
    }

}
