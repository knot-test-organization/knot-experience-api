package com.nttdata.knot.baseapi.Interfaces;
    
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC.IaC;

import reactor.core.publisher.Mono;

    public interface IIacService{
    
    Mono<IaC> createIacAsync(String org, String area, String product, Component component);

    Mono<DeleteGithubFileRequest> deleteIacAsync(String org, String area, String product, String name, String enviroment);

    Mono<IaC> updateIacAsync(String org, String area, String product, Component component);
    
    }