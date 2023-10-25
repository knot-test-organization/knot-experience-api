package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code;

import java.util.List;

import com.nttdata.knot.baseapi.Models.UserPackage.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Code {

    private boolean enabled;
    private String projectType;
    private String name;
    private String repoName;
    private String repoDescription;
    private String repoType;
    private String organizationName;
    private Boolean sonarqubeScan;
    private String projectLanguage;
    private String automationTool;
    private String gitBranching;
    private String containerRegistry;
    private String artifactRegistry;
    private List<User> users;
    private List<String> devcontainers;
    private List<Codespace> codespaces;


    public Code() {

    }
}

