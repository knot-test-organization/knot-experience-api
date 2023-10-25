package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class RootDeployment {

    @JsonProperty("hpa")
    private HPA hpa;

}
