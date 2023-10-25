package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Autoscaling {

    private Boolean enabled;
    private int minReplicas;
    private int maxReplicas;
    private int targetAverageUtilization;
    private String targetMemoryAverageUtilization;


}