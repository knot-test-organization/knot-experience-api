package com.nttdata.knot.baseapi.Services.VerticalServices;

import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.knot.baseapi.Interfaces.IAlmService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GitflowPackage.ComponentRelease;
import com.nttdata.knot.baseapi.Models.GitflowPackage.Release;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.ALM.ALM;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineResponseStatus;

import reactor.core.publisher.Mono;

@Service
public class AlmService implements IAlmService {

    private static final Logger logger = LoggerFactory.getLogger(ALM.class);
    public WebClient webClient;

    @Autowired
    public AlmService(@Qualifier("apiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ALM> createAlmAsync(String org, String area, String product, Component component) {

        return webClient.post()
                .uri("/alm-api/alm/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(ALM.class)
                .doOnSuccess(response -> logger.info("ALM vertical created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

    @Override
    public Mono<DeleteGithubFileRequest> deleteAlmAsync(String org, String area, String product, String id) {
        return webClient
                .delete()
                .uri("/alm-api/alm/" + org + "/" + area + "/" + product + "/" + id)
                .retrieve()
                .bodyToMono(DeleteGithubFileRequest.class)
                .doOnSuccess(response -> logger.info("ALM vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<ALM> updateAlmAsync(String org, String area, String product, Component component) {
        return webClient
                .put()
                .uri("/alm-api/alm/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(ALM.class)
                .doOnSuccess(response -> logger.info("ALM vertical updated"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<PipelineResponseStatus>> getPipelineInfoAsync(PipelineRequestStatus pipelineRequestStatus) {
        return webClient.post()
                .uri("/alm-api/alm/getPipelineStatus")
                .bodyValue(pipelineRequestStatus)
                .retrieve()
                .bodyToFlux(PipelineResponseStatus.class)
                .collectList()
                .doOnSuccess(response -> logger.info("Searched within the data of the pipelines successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<String>> listPipelineExecutionsAsync(String pipelineName) {
        return webClient.get()
                .uri("/alm-api/alm/listPipelineExecutions/" + pipelineName)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .doOnSuccess(response -> logger.info("Retrieved pipeline executions successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<String>> listPipelineStepsAsync(String executionName) {
        return webClient.get()
                .uri("/alm-api/alm/listPipelineSteps/" + executionName)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .doOnSuccess(response -> logger.info("Retrieved pipeline steps successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<PipelineResponseStatus>> getInitialTableAsync(String componentName) {
       return webClient.get()
                .uri("/alm-api/alm/getInitialTable/" + componentName)
                .retrieve()
                .bodyToFlux(PipelineResponseStatus.class)
                .collectList()
                .doOnSuccess(response -> logger.info("Retrieved inital table successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<Release>> listReleases(Component component) {
        return webClient.post()
                .uri("/alm-api/alm/listReleases")
                .bodyValue(component)
                .retrieve()
                .bodyToFlux(Release.class)
                .collectList()
                .doOnNext(response -> logger.info("Request sent"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Release> createRelease(ComponentRelease componentRelease) {
        return webClient.post()
                .uri("/alm-api/alm/createRelease")
                .bodyValue(componentRelease)
                .retrieve()
                .bodyToMono(Release.class)
                .doOnSuccess(response -> logger.info("Searched within the data of the pipelines successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Release> promoteRelease(ComponentRelease componentRelease) {
        return webClient.put()
                .uri("/alm-api/alm/promoteRelease")
                .bodyValue(componentRelease)
                .retrieve()
                .bodyToMono(Release.class)
                .doOnSuccess(response -> logger.info("Searched within the data of the pipelines successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Release> approvePreRelease(ComponentRelease componentRelease) {
        return webClient.put()
                .uri("/alm-api/alm/approvePreRelease")
                .bodyValue(componentRelease)
                .retrieve()
                .bodyToMono(Release.class)
                .doOnSuccess(response -> logger.info("Searched within the data of the pipelines successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Release> approveProRelease(ComponentRelease componentRelease) {
        return webClient.put()
                .uri("/alm-api/alm/approveProRelease")
                .bodyValue(componentRelease)
                .retrieve()
                .bodyToMono(Release.class)
                .doOnSuccess(response -> logger.info("Searched within the data of the pipelines successfully"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

}
