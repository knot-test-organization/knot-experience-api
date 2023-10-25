package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project;

import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Metadata;

// project class
public class Project {
    private Metadata metadata;
    private ProjectSpec spec;
     
    
    public Project() {
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ProjectSpec getSpec() {
        return spec;
    }

    public void setSpec(ProjectSpec spec) {
        this.spec = spec;
    }

}