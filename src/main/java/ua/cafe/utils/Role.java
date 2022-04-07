package ua.cafe.utils;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class Role {

    private final boolean isAuthorised;
    private boolean isAdmin = false;

    public Role(HttpServletRequest request) {
        isAdmin = request.isUserInRole("ROLE_ADMIN");
        isAuthorised = isAdmin || request.isUserInRole("ROLE_WAITER") || request.isUserInRole("ROLE_COOK");
    }

    public Role() {
        isAuthorised = false;
    }
}
