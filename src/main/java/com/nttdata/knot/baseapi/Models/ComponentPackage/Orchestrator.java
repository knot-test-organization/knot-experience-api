package com.nttdata.knot.baseapi.Models.ComponentPackage;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Orchestrator {

    @JsonProperty("tool")
    private List<Tool> tool;

    @JsonProperty("gitBranching")
    private List<GitBranching> gitBranching;


}
