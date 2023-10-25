package com.nttdata.knot.baseapi.Services.VerticalServices;

import com.nttdata.knot.baseapi.Interfaces.ICollaborationService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Collaboration.Collaboration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CollaborationService implements ICollaborationService{

    private static final Logger logger = LoggerFactory.getLogger(Collaboration.class);
    public WebClient webClient;

    @Autowired
    public CollaborationService(@Qualifier("apiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Collaboration> createCollaborationAsync(String org, String area, String product, Component component) {
        return webClient.post()
                .uri("/collaboration-api/collaboration/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Collaboration.class)
                .doOnSuccess(response -> logger.info("Collaboration vertical created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

    @Override
    public Mono<DeleteGithubFileRequest> deleteCollaborationAsync(String org, String area, String product, String name) {
        return webClient
                .delete()
                .uri("/collaboration-api/collaboration/" + org + "/" + area + "/" + product + "/" + name)
                .retrieve()
                .bodyToMono(DeleteGithubFileRequest.class)
                .doOnSuccess(response -> logger.info("Collaboration vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Collaboration> updateCollaborationAsync(String org, String area, String product, Component component) {
         return webClient
                .put()
                .uri("/collaboration-api/collaboration/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Collaboration.class)
                .doOnSuccess(response -> logger.info("Collaboration vertical updated"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }
    
}
