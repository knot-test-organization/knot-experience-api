package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RootALM {

    @JsonProperty("orchestrator")
    private Orchestrator orchestrator;

    @JsonProperty("imageRegistry")
    private ImageRegistry imageRegistry;

    @JsonProperty("artifactRegistry")
    private ArtifactRegistry artifactRegistry;

    @JsonProperty("codeAnalysis")
    private CodeAnalysis codeAnalysis;

}
