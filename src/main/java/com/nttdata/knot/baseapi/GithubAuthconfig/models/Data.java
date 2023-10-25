package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Data {
    @Setter
    public Organization organization;

    public Data(@JsonProperty(value = "organization") Organization organization) {
        this.organization = organization;
    }
}
