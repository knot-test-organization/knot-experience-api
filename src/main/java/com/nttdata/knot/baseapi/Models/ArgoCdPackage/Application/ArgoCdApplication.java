package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application;

import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Metadata;

public class ArgoCdApplication {
    private Metadata metadata;
    private ApplicationSpec spec;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ApplicationSpec getSpec() {
        return spec;
    }

    public void setSpec(ApplicationSpec spec) {
        this.spec = spec;
    }
}
