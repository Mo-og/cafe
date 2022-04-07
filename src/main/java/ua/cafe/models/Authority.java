package ua.cafe.models;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    DIRECTOR("ROLE_ADMIN", "Директор"),
    WAITER("ROLE_WAITER", "Официант"),
    COOK("ROLE_COOK", "Повар");

    //ROLE_ADMIN, etc.
    private final String authority;

    //Директор, etc.
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
