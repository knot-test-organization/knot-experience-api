package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IaC {

    private String name;
    private DatabaseDevOps database;
    private ServerlessDevOps serverless;

    public IaC() {

    }

    public IaC(String name, DatabaseDevOps database, ServerlessDevOps serverless) {
        this.name = name;
        this.database = database;
        this.serverless = serverless;
    }
}
