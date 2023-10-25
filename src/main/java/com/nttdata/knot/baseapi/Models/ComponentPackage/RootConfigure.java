package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nttdata.knot.baseapi.Models.UserPackage.ProjectRoles;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RootConfigure {
    @JsonProperty("types")
    private List<Types> types;
    @JsonProperty("projectroles")
    private List<ProjectRoles> projectRoles;
}

