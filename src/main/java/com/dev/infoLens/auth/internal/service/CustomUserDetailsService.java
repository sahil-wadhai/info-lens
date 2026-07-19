package com.dev.infoLens.auth.internal.service;

import com.dev.infoLens.user.authApi.AuthUser;
import com.dev.infoLens.user.authApi.AuthUserProviderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserProviderService userProviderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AuthUser user = userProviderService.findAuthUser(username);

            // Map your role string/enum to a GrantedAuthority collection
            String roleName = "ROLE_" + user.getRole().toString();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

            return new User(
                    user.getUsername(),
                    user.getPasswordHash(),
                    Collections.singletonList(authority)
            );
        } catch (Exception e) {
            // Spring Security requires UsernameNotFoundException to trigger proper bad credentials flow
            throw new UsernameNotFoundException("Could not find user: " + username, e);
        }
    }
}
