package com.nttdata.knot.baseapi.Models.BlueprintPackage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BlueprintConfigDeploymentHPA {
    private List<BlueprintConfigDeploymentHPATool> tool;

    public BlueprintConfigDeploymentHPA() {
    }

    public BlueprintConfigDeploymentHPA(List<BlueprintConfigDeploymentHPATool> tool) {
        this.tool = tool;
    }
}
