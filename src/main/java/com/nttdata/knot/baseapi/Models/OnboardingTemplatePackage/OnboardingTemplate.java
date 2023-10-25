package com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage;

import java.util.List;

import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.ALM.ALM;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Code;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Collaboration.Collaboration;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment.Deploy;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Enviroments.Envs;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC.IaC;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class OnboardingTemplate {
    private ALM alm;
    private Deploy deployment;
    private Code code;
    private IaC iac;
    private Collaboration collaboration;
    private List<Envs> environments;

    public OnboardingTemplate(ALM alm, Deploy deployment, Code code, IaC iac, Collaboration collaboration, List<Envs> environments) {
        this.alm = alm;
        this.deployment = deployment;
        this.code = code;
        this.iac = iac;
        this.collaboration = collaboration;
        this.environments = environments;
    }
}
