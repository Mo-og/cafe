package ua.cafe.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.cafe.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Getter
@Component
public class Role{

    public Role(HttpServletRequest request) {
        isAuthorised = request.isUserInRole("ROLE_WAITER")||request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_COOK");
        isAdmin = request.isUserInRole("ROLE_ADMIN");
        user = null;
    }

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }
    private static UserService userService;
    private final boolean isAuthorised;
    private final UserDetails user;
    private boolean isAdmin = false;


    public Role() {
        isAuthorised = false;
        user = null;
    }

    public Role(Principal principal) {
        if (principal == null) {
            isAuthorised = false;
            user = null;
            return;
        }
        user = userService.loadUserByUsername(principal.getName());
        String authority = user.getAuthorities().toString();
        switch (authority) {
            case "[ROLE_ADMIN]" -> {
                isAuthorised = true;
                isAdmin = true;
            }
            case "[ROLE_WAITER]", "[ROLE_COOK]" -> isAuthorised = true;
            default -> isAuthorised = false;
        }
    }
}
