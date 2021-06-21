package com.airbnb.authenticator.listeners;

import com.airbnb.authenticator.services.LoginAttemptService;
import com.airbnb.common.utils.NetworkUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    public void onApplicationEvent(@NotNull AuthenticationSuccessEvent e) {
        loginAttemptService.loginSucceeded(NetworkUtil.getClientIP());
    }
}
