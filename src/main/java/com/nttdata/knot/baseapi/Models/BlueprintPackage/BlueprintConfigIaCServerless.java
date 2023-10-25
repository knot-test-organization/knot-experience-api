package com.nttdata.knot.baseapi.Models.BlueprintPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlueprintConfigIaCServerless {

    @JsonProperty("createFunctionApp")
    private Boolean createFunctionApp;

    public BlueprintConfigIaCServerless() {
    }

    public BlueprintConfigIaCServerless(Boolean createFunctionApp) {
        this.createFunctionApp = createFunctionApp;
    }
}
