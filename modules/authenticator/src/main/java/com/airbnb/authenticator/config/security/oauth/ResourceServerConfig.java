package com.airbnb.authenticator.config.security.oauth;

import com.airbnb.authenticator.domains.privilege.models.dtos.PrivilegeDto;
import com.airbnb.authenticator.domains.privilege.models.entities.Privilege;
import com.airbnb.authenticator.domains.privilege.models.enums.Privileges;
import com.airbnb.authenticator.domains.privilege.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final PrivilegeService privilegeService;

    @Autowired
    public ResourceServerConfig(TokenStore tokenStore, PrivilegeService privilegeService) {
        this.tokenStore = tokenStore;
        this.privilegeService = privilegeService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String ADMINISTRATION = "ADMINISTRATION";
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry r = http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/api/v1/register/**",
                        "/api/v1/search/**",
                        "/api/v2/search/**",
                        "/api/v1/login**",
                        "/oauth/token**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/sw/**",
                        "/api/v1/reset_password/**",
                        "/api/v1/check_token_validity",
                        "/api/v1/promos/**",
                        "/swagger-ui.html",
                        "/api/v1/profiles/user/snap/*",
                        "/api/v1/public/**"

                )
                .permitAll()
                .antMatchers(
                        "/api/v1/admin/**"
                )
                .hasAnyAuthority(ADMINISTRATION);
//        for (Privilege p : this.privilegeService.findAll())
        r.antMatchers("/api/v1/**").hasAnyAuthority(Privileges.ACCESS_USER_RESOURCES.name());

        r.anyRequest()
//                .authenticated()
                .hasAuthority(ADMINISTRATION)
                .and().logout().logoutSuccessUrl("/").permitAll();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(this.tokenStore);
    }
}
