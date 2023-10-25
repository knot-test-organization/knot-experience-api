package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class GitBranching {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}
