package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project;

import lombok.Getter;
import lombok.Setter;

// Destination class
@Getter @Setter 
public class ProjectDestination {
    private String server;
    private String name;
    private String namespace;
    
    public ProjectDestination(String server, String name, String namespace) {
        this.server = server;
        this.name = name;
        this.namespace = namespace;
    }
}

