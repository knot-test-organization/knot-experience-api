package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DatabaseDevOps {

    private Boolean createDatabase;
    private String type;
    private String tier;
    private String dbVersion;
    private String name;
    private String user;
    private String password;

    public DatabaseDevOps(Boolean createDatabase, String type, String tier, String dbVersion, String name, String user, String password) {
        this.createDatabase = createDatabase;
        this.type = type;
        this.tier = tier;
        this.dbVersion = dbVersion;
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public DatabaseDevOps() {
    }
}
