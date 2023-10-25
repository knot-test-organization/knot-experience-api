package com.nttdata.knot.baseapi.GithubAuthconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GithubTokenIntrospector implements OpaqueTokenIntrospector {

    private final IAuthenticationService authenticationService;

    @Autowired
    public GithubTokenIntrospector(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        GithubUserInfo userInfoDTO = null;
        try {
            userInfoDTO = authenticationService.introspect(token);
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        Collection<GrantedAuthority> authorities = userInfoDTO
                .getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("USER_INFO_DTO", userInfoDTO);

        return new OAuth2IntrospectionAuthenticatedPrincipal(attributes, authorities);
    }
}
