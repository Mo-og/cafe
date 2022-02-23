package ua.cafe.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    DIRECTOR("ROLE_ADMIN", "Директор"),
    WAITER("ROLE_WAITER", "Официант"),
    COOK("ROLE_COOK", "Повар");

    private final String authority;
    private final String name;

    Authority(String authority, String name) {
        this.authority = authority;
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getName() {
        return name;
    }
}
