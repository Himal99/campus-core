package com.sb.file.compressor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.core.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long> implements UserDetails {

    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private String phoneNumber;

    @Transient
    private String fullName;

    private Boolean serverCompressor = Boolean.FALSE;


    private String userProfile;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;

    @Transient
    private String userRole;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToOne(cascade = CascadeType.ALL)
    private UserConfiguration userConfiguration;

    private Boolean hasChangedPassword = Boolean.FALSE;
    private Boolean hasResetPassword = Boolean.FALSE;


    public String getUserRole() {
        for (Role role : this.getRoles()) {
            userRole = role.getName().getValue();
        }
        return userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : this.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName().getValue()));
        }
        return authorities;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return "";
    }


    @JsonIgnore
    public String getEncodedPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
