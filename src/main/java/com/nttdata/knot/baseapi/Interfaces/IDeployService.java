package com.nttdata.knot.baseapi.Interfaces;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment.Deploy;

import reactor.core.publisher.Mono;

    public interface IDeployService{
    
    Mono<Deploy> createDeployAsync(String org, String area, String product, Component component);

        Mono<DeleteGithubFileRequest> deleteDeployAsync(String org, String area, String product, String name, String enviroment);

    Mono<Deploy> updateDeployAsync(String org, String area, String product, Component component);
    
    }
