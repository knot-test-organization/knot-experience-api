package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.ALM;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ALM {

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
    private String pipelineTemplate;
    private String gitBranching;
    private String containerRegistry;
    private String artifactRegistry;
    private String idProduct;
    private String organizationProduct;
    private String areaProduct;

    public ALM() {
    }
}

    
