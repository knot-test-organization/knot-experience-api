package com.nttdata.knot.baseapi.Interfaces;

    
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Collaboration.Collaboration;

import reactor.core.publisher.Mono;

    public interface ICollaborationService {
    
    Mono<Collaboration> createCollaborationAsync(String org, String area, String product, Component component);

    Mono<DeleteGithubFileRequest> deleteCollaborationAsync(String org, String area, String product, String name);

    Mono<Collaboration> updateCollaborationAsync(String org, String area, String product, Component component);
    
    }