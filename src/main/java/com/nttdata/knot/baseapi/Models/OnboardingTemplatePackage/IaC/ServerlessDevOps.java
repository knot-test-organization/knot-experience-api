package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerlessDevOps {
    @JsonProperty("createFunctionApp")
    private Boolean createFunctionApp;

    @JsonProperty("applicationStack")
    private String applicationStack;

    public ServerlessDevOps() {
    }

    public ServerlessDevOps(Boolean createFunctionApp, String applicationStack) {
        this.createFunctionApp = createFunctionApp;
        this.applicationStack = applicationStack;
    }
}
