package com.airbnb.authenticator.config.security;

import com.airbnb.authenticator.domains.users.models.entities.User;
import com.airbnb.authenticator.domains.users.repositories.UserRepository;
import com.airbnb.authenticator.models.UserAuth;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public final class SecurityContext {

    private static UserRepository userRepo;
    private static UserAuth loggedInUser;

    public SecurityContext(UserRepository userRepo) {
        SecurityContext.userRepo = userRepo;
    }

//    @Autowired
//    private UserRepository uRepo;
//
//    @PostConstruct
//    public void init() {
//        SecurityContext.userRepository = uRepo;
//    }

    public static void updateAuthentication(UserAuth user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getPrincipal() instanceof String)
                return (String) authentication.getPrincipal();
            else
                return ((UserAuth) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

//    public static UserAuth getCurrentUser() {
//        String username = getLoggedInUsername();
//        User user = userRepo.find(username).orElse(null);
//        return user == null ? null : new UserAuth(user);
//    }

    public static UserAuth getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getPrincipal() instanceof String) {
                if (loggedInUser == null || !authentication.getPrincipal().equals(loggedInUser.getUsername())) {
                    Optional<User> user = SecurityContext.userRepo.findByUsername((String) authentication.getPrincipal());
                    if (user.isEmpty()) return null;
                    loggedInUser = new UserAuth(user.get());
                }
                return loggedInUser;
            }
            return (UserAuth) authentication.getPrincipal();
        }
        return null;
    }

    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    public static String getToken() {
        String token = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        }
        return token;
    }
}
