package com.nttdata.knot.baseapi.Services;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.knot.baseapi.Interfaces.IArgoCDService;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;

import reactor.core.publisher.Mono;

@Service
public class ArgoCDService implements IArgoCDService {

    private static final Logger logger = LoggerFactory.getLogger(ArgoCDService.class);


    public WebClient webClient;

    @Autowired
    // Added @Qualifier("") because we have multiple webClient beans
    public ArgoCDService(@Qualifier("argoCDWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<String> getArgoCDApplicationListAsync() throws SSLException {
        return webClient
                .get()
                .uri("/applications")
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("ArgoCD response Ok"))
                .doOnError(e -> logger.error("Unable to process request, exception is " +
                        e.getMessage()));
    }

    public Mono<String> getArgoCDApplicationAsync(String name) {
        return webClient
                .get()
                .uri("/applications/{name}", name)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("ArgoCD response Ok"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<ArgoCdProject> createArgoCDProjectAsync(ArgoCdProject argoCdProject) {

        return webClient
                .post()
                .uri("/projects")
                .bodyValue(argoCdProject)
                .retrieve()
                .bodyToMono(ArgoCdProject.class)
                .doOnSuccess(response -> logger.info("ArgoCD Project created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }
    @Override
    public Mono<String> deleteArgoCDProjectAsync(String name) {

        return webClient
                .delete()
                .uri("/projects/" + name)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("ArgoCD Project created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<ArgoCdApplication> createArgoCDApplicationAsync(ArgoCdApplication argoCDApplication) {

        return webClient
                .post()
                .uri("/applications")
                .bodyValue(argoCDApplication)
                .retrieve()
                .bodyToMono(ArgoCdApplication.class)
                .doOnSuccess(response -> logger.info("ArgoCD Application created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

    @Override
    public Mono<String> deleteArgoCDApplicationAsync(String name) {
        return webClient
                .delete()
                .uri("/applications/" + name)               
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("Status Code = " + response))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<ArgoCdApplication> updateArgoCDApplicationAsync(ArgoCdApplication argoCDApplication, String name) {
        return webClient
                .put()
                .uri("/applications/" + name)
                .bodyValue(argoCDApplication)
                .retrieve()
                .bodyToMono(ArgoCdApplication.class)
                .doOnSuccess(response -> logger.info("Status Code = " + response))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<ArgoCdApplication> getArgoCDApplicationByRefreshAsync(String name) {
        return webClient
                .get()
                .uri("/applications/" + name + "?refresh=true")
                .retrieve()
                .bodyToMono(ArgoCdApplication.class)
                .doOnSuccess(response -> logger.info("ArgoCD response Ok"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

}
