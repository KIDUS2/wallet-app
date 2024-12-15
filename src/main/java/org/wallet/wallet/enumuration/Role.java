package org.wallet.wallet.enumuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.wallet.wallet.enumuration.Permission.*;


@RequiredArgsConstructor
public enum Role {


      USER(Collections.emptySet()),
      Super_Administrator(Collections.emptySet()),
      Service_Center_Agent(Collections.emptySet()),
      Financial_Institution (Collections.emptySet()),
      MNO(Collections.emptySet()),
      Agent(Collections.emptySet()),
      Customer(Collections.emptySet()),

    ADMIN(
                Set.of(
                        ADMIN_READ,
                        ADMIN_UPDATE,
                        ADMIN_DELETE,
                        ADMIN_CREATE

                        )
        )

    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
