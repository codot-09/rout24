package com.example.rout24.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.rout24.entity.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users", indexes = {
    @Index(name = "idx_tg_username", columnList = "tgUsername"),
    @Index(name = "idx_role", columnList = "role"),
    @Index(name = "idx_blocked", columnList = "blocked")
})
public class User implements UserDetails{

    @Id
    private String chatId;

    @Column(unique = true)
    private String tgUsername;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String imageUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate registeredAt;

    private boolean blocked;
    private boolean premiumUser;

    @UpdateTimestamp
    private LocalDate lastLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null ? List.of(() -> "ROLE_" + role.name()) : List.of();
    }

    @Override
    public String getUsername(){
        return chatId;
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !blocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !blocked;
    }
}
