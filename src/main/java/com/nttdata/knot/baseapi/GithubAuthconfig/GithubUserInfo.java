package com.nttdata.knot.baseapi.GithubAuthconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserInfo {

    private final Integer id;
    private final String login;
    @Setter
    private List<String> authorities;
    @Setter
    private List<String> scope;

    public GithubUserInfo(@JsonProperty(value = "id") Integer id,
                       @JsonProperty(value = "login") String login,
                       @JsonProperty(value = "authorities") List<String> authorities,
                       @JsonProperty(value = "scope") List<String> scope) {

        this.id = id;
        this.login = login;
        this.authorities = authorities;
        this.scope = scope;
    }
}
