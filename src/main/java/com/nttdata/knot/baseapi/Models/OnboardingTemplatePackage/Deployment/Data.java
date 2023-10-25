package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Data {

    private String type;
    private String tier;
    private String dbVersion;
    private String name;
    private String user;
    private String password;

}
