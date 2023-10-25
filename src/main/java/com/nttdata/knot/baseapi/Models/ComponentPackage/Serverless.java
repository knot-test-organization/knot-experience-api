package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Serverless {

    @JsonProperty("createFunctionApp")
    private Boolean createFunctionApp;

    @JsonProperty("applicationStack")
    private String applicationStack;

    public Serverless() {
    }

    public Serverless(Boolean createFunctionApp, String applicationStack) {
        this.createFunctionApp = createFunctionApp;
        this.applicationStack = applicationStack;
    }
}
