package com.nttdata.knot.baseapi.Models.BlueprintPackage;

import com.nttdata.knot.baseapi.Models.ComponentPackage.DB_Variable;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlueprintConfigIaC {
    private List<DB_Variable> databases;
    private BlueprintConfigIaCServerless serverless;

    public BlueprintConfigIaC() {
    }

    public BlueprintConfigIaC(List<DB_Variable> databases, BlueprintConfigIaCServerless serverless) {
        this.databases = databases;
        this.serverless = serverless;
    }
}
