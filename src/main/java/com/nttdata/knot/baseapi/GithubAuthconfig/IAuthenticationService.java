package com.nttdata.knot.baseapi.GithubAuthconfig;

import javax.net.ssl.SSLException;

public interface IAuthenticationService {
    GithubUserInfo introspect(String token) throws SSLException;
}
