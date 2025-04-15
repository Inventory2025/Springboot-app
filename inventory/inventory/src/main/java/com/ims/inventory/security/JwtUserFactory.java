package com.ims.inventory.security;

import com.ims.inventory.domen.entity.RoleMaster;
import com.ims.inventory.domen.entity.UserMaster;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(UserMaster user) {
        JwtUser jwtUser = null;
        if (user != null) {
            long expTime = 1000;
            String roleId = "";
            RoleMaster role = user.getRoleMaster();
            if (role != null) {
                roleId = role.getId();
                expTime = expTime < role.getExpiryTime() ? role.getExpiryTime() : expTime;
            }
            jwtUser = new JwtUser(user.getId(), user.getUsername(), user.getPassword(),
                    new Timestamp(System.currentTimeMillis()),
                    mapToGrantedAuthorities(user.getRoleMaster()),
                    true, user.getFirstName(),
                    roleId,
                    expTime, user.getPhoneNumber(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis())
            );
        }
        return jwtUser;
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(RoleMaster userRoleList) {
        List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
        grantedAuthority.add(new SimpleGrantedAuthority(userRoleList.getId()));
        return grantedAuthority;
    }

}
