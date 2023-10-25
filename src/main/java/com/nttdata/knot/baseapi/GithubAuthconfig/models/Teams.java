package com.nttdata.knot.baseapi.GithubAuthconfig.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Teams {
    @Setter
    public List<Edge> edges;

    public Teams(@JsonProperty(value = "edges") List<Edge> edges) {
        this.edges = edges;
    }
}
