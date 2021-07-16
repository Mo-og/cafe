package ua.cafe.entities;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.cafe.services.UserService;

import java.security.Principal;
import java.util.Arrays;

@Getter
@Component
public class Role {
    public static final String[] ROLES = {"[ROLE_WAITER]", "[ROLE_ADMIN]", "[ROLE_COOK]"};

    public static boolean checkIfAuthorised(String inputAuthorities) {
        return Arrays.stream(ROLES).anyMatch(inputAuthorities::contains);
    }

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
        user=null;
    }

    public Role(Principal principal) {
        if (principal == null) {
            isAuthorised = false;
            user=null;
            return;
        }
        user = userService.loadUserByUsername(principal.getName());
        String roleName=user.getAuthorities().toString();
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
