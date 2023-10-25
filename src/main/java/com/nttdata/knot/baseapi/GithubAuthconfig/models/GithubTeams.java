
package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GithubTeams {
    @Setter
    public Data data;

    public GithubTeams(@JsonProperty(value = "data") Data data) {
        this.data = data;
    }
}