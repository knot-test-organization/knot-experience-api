package com.nttdata.knot.baseapi.Services.VerticalServices;

import com.nttdata.knot.baseapi.Interfaces.IIacService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC.IaC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;


@Service
public class IacService implements IIacService{
    
    private static final Logger logger = LoggerFactory.getLogger(IaC.class);
    public WebClient webClient;

    @Autowired
    public IacService(@Qualifier("apiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<IaC> createIacAsync(String org, String area, String product, Component component) {
        return webClient.post()
                .uri("/iac-api/iac/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(IaC.class)
                .doOnSuccess(response -> logger.info("IaC vertical created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }
    @Override
    public Mono<DeleteGithubFileRequest> deleteIacAsync(String org, String area, String product, String name, String enviroment) {
        return webClient
                .delete()
                .uri("/iac-api/iac/" + org + "/" + area + "/" + product + "/" + name + "/" + enviroment)
                .retrieve()
                .bodyToMono(DeleteGithubFileRequest.class)
                .doOnSuccess(response -> logger.info("IaC vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }


    @Override
    public Mono<IaC> updateIacAsync(String org, String area, String product, Component component) {
        return webClient
                .put()
                .uri("/iac-api/iac/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(IaC.class)
                .doOnSuccess(response -> logger.info("IaC vertical updated"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    

    }
    
}
