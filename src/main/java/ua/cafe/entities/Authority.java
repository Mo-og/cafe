package ua.cafe.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    DIRECTOR("ROLE_ADMIN", "Директор"), WAITER("ROLE_WAITER", "Официант"), COOK("ROLE_COOK", "Повар");

    private final String authority;
    private final String position;

    Authority(String authority, String position) {
        this.authority = authority;
        this.position = position;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getPosition() {
        return position;
    }
}
