package ua.cafe.entities;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Role {
    public static final String[] ROLES={"[ROLE_WAITER]","[ROLE_ADMIN]","[ROLE_COOK]"};
    public static boolean isAuthorised(String inputAuthorities) {
        return Arrays.stream(ROLES).anyMatch(inputAuthorities::contains);
    }
    /*private final boolean isAuthorised;
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
    }*/
}
