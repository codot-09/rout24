package org.example.rout24.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.example.rout24.entity.base.BaseEntity;
import org.example.rout24.entity.enums.Role;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Where(clause = "deleted = false")
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String chatId;

    private String phone;
    private String password;
    private String fullName;
    private String region;
    private String city;
    private Role role;
    private String imageUrl;
    private boolean telegramAuth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getUsername() {
        if (telegramAuth) return chatId;
        return phone;
    }

    @Override
    public String getPassword() {
        if (telegramAuth) return "";
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
