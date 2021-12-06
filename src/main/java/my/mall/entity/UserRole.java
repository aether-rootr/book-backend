package my.mall.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 角色表
 */
@Data
public class UserRole {

    @JsonProperty(value = "login_name")
    private String loginName;

    @JsonProperty(value = "password_md5")
    private String passwordMd5;

    @JsonProperty(value = "role_name")
    private String roleName;


}
