package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Deploy {
    private String name;
    private Image image;
    private int replicaCount;
    private Autoscaling autoscaling;
    private Istio istio;
    private Secret secret;
    private String technology;


    public Deploy() {

    }
}
