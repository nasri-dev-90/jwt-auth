package com.example.jwtToken.user;

import com.example.jwtToken.Permission.Permission;
import com.example.jwtToken.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // جلوگیری از تداخل با کلمه کلیدی رزرو شده user در دیتابیس‌ها
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String nationalId; // به عنوان Username استفاده می‌شود

    private String password;
    private String address;
    private String fatherName;
    private LocalDate birthDate;
    private LocalDate membershipDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> directPermissions = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new java.util.HashSet<>();

        if (roles != null) {
            for (Role role : roles) {

                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                // اضافه کردن تک تک مجوزهای داخل آن نقش به لیست اتوریتی‌ها
                for (Permission permission : role.getPermissions()) {
                    String authorityName = permission.getResource().getName() + "_" + permission.getAction().getName();
                    authorities.add(new SimpleGrantedAuthority(authorityName));
                }
            }
        }
        if (directPermissions != null) {
            for (Permission permission :directPermissions) {
                String authorityName = permission.getResource().getName() + "_" + permission.getAction().getName();
                authorities.add(new SimpleGrantedAuthority(authorityName));
            }
        }
        return authorities;
    }


    @Override
    public String getUsername() {
        return nationalId;
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