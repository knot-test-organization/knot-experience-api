package com.nttdata.knot.baseapi.Models.BlueprintPackage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BlueprintConfigCollaboration {
    private BlueprintConfigCollaborationConnectivity connectivity;

    public BlueprintConfigCollaboration() {
    }

    public BlueprintConfigCollaboration(BlueprintConfigCollaborationConnectivity connectivity) {
        this.connectivity = connectivity;
    }
}
