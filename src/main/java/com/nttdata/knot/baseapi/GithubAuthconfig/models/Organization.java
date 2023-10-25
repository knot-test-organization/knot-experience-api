package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Organization {
    @Setter
    public Teams teams;

    public Organization(@JsonProperty(value = "teams") Teams teams) {
        this.teams = teams;
    }
}
