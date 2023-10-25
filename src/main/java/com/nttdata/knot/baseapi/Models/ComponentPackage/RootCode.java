package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RootCode {
    @JsonProperty("repository")
    private List<Types> repository;
    @JsonProperty("technologies")
    private List<Technology> technologies;
}

