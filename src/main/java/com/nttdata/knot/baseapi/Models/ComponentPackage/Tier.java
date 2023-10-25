package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Tier {

    @JsonProperty("simpleType")
    private String simpleType;
    @JsonProperty("complexType")
    private String complexType;

    public Tier() {
    }

    public Tier(String simpleType, String complexType) {
        this.simpleType = simpleType;
        this.complexType = complexType;
    }
}
