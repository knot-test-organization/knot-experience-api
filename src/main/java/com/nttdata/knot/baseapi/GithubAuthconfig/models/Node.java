package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Node {
    @Setter
    public String name;

    public Node(@JsonProperty(value = "name") String name) {
        this.name = name;
    }
}
