package com.nttdata.knot.baseapi.Models.ComponentPackage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Env {
    private boolean enabled;
    private String envPath;
    private String nameSpace;
    private String version;


}