package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Image {

    private String repository;
    private String tag;
    private String envPath;
    private String nameSpace;

}

