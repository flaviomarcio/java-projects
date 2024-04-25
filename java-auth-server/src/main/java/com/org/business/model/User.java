package com.org.business.model;

import com.org.business.dto.SignupIn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "user")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID scopeId;
    @Column(nullable = false)
    private LocalDateTime dt;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 200)
    private String password;
    @Column(nullable = false, length = 200)
    private String passwordCrypt;
    @Column(nullable = false, length = 20)
    private String document;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 20)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDate dtBirth;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private boolean deleted;

    public User() {

    }

    public static User cast(Object userDetail) {
        return (User) userDetail;
    }


    public static User from(User user) {
        return user == null
                ? null
                : User
                .builder()
                .id(user.getId())
                .dt(user.getDt())
                .scopeId(user.getScopeId())
                .username(user.getUsername())
                .password(user.getPassword())
                .passwordCrypt(user.getPasswordCrypt())
                .document(user.getDocument())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .enabled(user.isEnabled())
                .deleted(user.isDeleted())
                .build();
    }

    public static User from(SignupIn signupIn) {
        return signupIn == null
                ? null
                : User
                .builder()
                .username(signupIn.getUsername())
                .password(signupIn.getPassword())
                .document(signupIn.getDocument())
                .email(signupIn.getEmail())
                .phoneNumber(signupIn.getPhoneNumber())
                .enabled(true)
                .deleted(false)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
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
