package com.nttdata.knot.baseapi.Models.ComponentPackage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CodeAnalysis {

    @JsonProperty("tool")
    private List<Tool> tool;

}