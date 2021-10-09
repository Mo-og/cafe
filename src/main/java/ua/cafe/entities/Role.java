package ua.cafe.entities;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.cafe.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;

@Getter
@Component
public class Role {
//    public static final String[] ROLES = {"[ROLE_WAITER]", "[ROLE_ADMIN]", "[ROLE_COOK]"};
//    public static final String[] ROLES1 = {"ROLE_WAITER", "ROLE_ADMIN", "ROLE_COOK"};
    public static final String[] ROLES = {"WAITER", "ADMIN", "COOK"};

    public Role(HttpServletRequest request) {
        isAuthorised = request.isUserInRole("ROLE_WAITER")||request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_COOK");
        isAdmin = request.isUserInRole("ROLE_ADMIN");
        user = null;
    }

    public static boolean isAuthorised(String inputAuthorities) {
        return Arrays.stream(ROLES).anyMatch(inputAuthorities::contains);
    }

    /*public static boolean isAuthorized(Principal principal) {
        if (principal == null)
            return false;
        User user = userService.getByUsername(principal.getName());
        return isAuthorized(user.getRoles());
    }*/

    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

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
        String roleName = user.getAuthorities().toString();
        switch (roleName) {
            case "[ROLE_ADMIN]" -> {
                isAuthorised = true;
                isAdmin = true;
            }
            case "[ROLE_WAITER]", "[ROLE_COOK]" -> isAuthorised = true;
            default -> isAuthorised = false;
        }
    }
}
