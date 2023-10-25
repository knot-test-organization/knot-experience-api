package com.nttdata.knot.baseapi.Models.BlueprintPackage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BlueprintConfigDeployment {
    private BlueprintConfigDeploymentHPA hpa;

    public BlueprintConfigDeployment() {
    }

    public BlueprintConfigDeployment(BlueprintConfigDeploymentHPA hpa) {
        this.hpa = hpa;
    }
}
