package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
public class Edge {

    @Setter
    public Node node;

    public Edge(@JsonProperty(value = "node") Node node) {
        this.node = node;
    }
}
