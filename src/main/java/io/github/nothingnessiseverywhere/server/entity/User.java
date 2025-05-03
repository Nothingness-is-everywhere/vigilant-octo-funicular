package io.github.nothingnessiseverywhere.server.entity;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    // Setters
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // 实现 UserDetails 接口方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 根据实际情况返回权限
    }

}