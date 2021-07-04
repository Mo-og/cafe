package ua.cafe.entities;

import lombok.Getter;

@Getter
public class Role {
    private final boolean isAuthorised;
    private boolean isAdmin = false;

    public Role(String roleName) {
        switch (roleName) {
            case "ROLE_ADMIN" -> {
                isAuthorised = true;
                isAdmin = true;
            }
            case "ROLE_WAITER", "ROLE_COOK" -> isAuthorised = true;
            default -> isAuthorised = false;
        }
    }
}
