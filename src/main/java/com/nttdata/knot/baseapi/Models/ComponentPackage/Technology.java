package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Technology {

    @JsonProperty("label")
    private String label;

    @JsonProperty("id")
    private String id;

     @JsonProperty("parentType")
    private String parentType;

 
}
