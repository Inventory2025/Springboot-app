package com.ims.inventory.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

public class JwtUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final String id;
    private final String firstName;
    private final String username;
    private final String password;
    private Timestamp lastLoginTime;
    private String roleId;
    private long expTime;
    private String contactNumber;
    private Timestamp modifiedOn;
    private Timestamp createdOn;

    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
//    private final Date lastPasswordResetDate;

    public JwtUser(
            String id,
            String username,
            String password,
            Timestamp lastLoginTime,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled,
            String firstName,
            String roleId,
            long expTime,
            String contactNumber,
            Timestamp modifiedOn,
            Timestamp createdOn
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastLoginTime = lastLoginTime;
        this.firstName = firstName;
        this.roleId = roleId;
        this.expTime = expTime;
        this.contactNumber = contactNumber;
        this.modifiedOn = modifiedOn;
        this.createdOn = createdOn;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {

        this.lastLoginTime = lastLoginTime;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
}
