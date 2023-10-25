package com.nttdata.knot.baseapi.Interfaces;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Code;

import reactor.core.publisher.Mono;

    public interface  ICodeService {
    
    Mono<Code> createCodeAsync(String org, String area, String product, Component component);

    Mono<DeleteGithubFileRequest> deleteCodeAsync(String org, String area, String product, String name);

    Mono<Code> updateCodeAsync(String org, String area, String product, Component component);
    
    }
