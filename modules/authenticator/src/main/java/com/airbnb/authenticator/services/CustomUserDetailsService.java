package com.airbnb.authenticator.services;

import com.airbnb.authenticator.domains.users.models.entities.User;
import com.airbnb.authenticator.domains.users.repositories.UserRepository;
import com.airbnb.authenticator.models.UserAuth;
import com.airbnb.common.exceptions.notfound.UserNotFoundException;
import com.airbnb.common.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author mir00r on 21/6/21
 * @project IntelliJ IDEA
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepo, LoginAttemptService loginAttemptService) {
        this.userRepo = userRepo;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // flood control
        String ip = NetworkUtil.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
        // end flood control
        User user;
        try {
            user = this.userRepo.find(username).orElseThrow(() -> new UserNotFoundException("Could not find user with username: " + username));
            if (user == null) throw new UserNotFoundException("User doesn't exist!");
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User doesn't exist!");
        }
        return new UserAuth(user);
    }

}
