package com.example.jwtToken.Permission;

import com.example.jwtToken.action.Action;
import com.example.jwtToken.resource.Resource;
import com.example.jwtToken.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"resource_id", "action_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "action_id", nullable = false)
    private Action action;



    /**
     * یک متد کمکی (Helper) برای تولید فرمت رشته‌ای که
     * Spring Security در GrantedAuthority به آن نیاز دارد.
     * خروجی مثلا: TICKET:CREATE
     */
    public String getAuthorityValue() {
        return this.resource.getName() + "_" + this.action.getName();
    }
}
