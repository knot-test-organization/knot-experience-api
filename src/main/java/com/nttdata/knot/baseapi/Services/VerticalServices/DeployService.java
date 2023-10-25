package com.nttdata.knot.baseapi.Services.VerticalServices;

import com.nttdata.knot.baseapi.Interfaces.IDeployService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment.Deploy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class DeployService implements IDeployService {

    private static final Logger logger = LoggerFactory.getLogger(Deploy.class);
    public WebClient webClient;

    @Autowired
    public DeployService(@Qualifier("apiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Deploy> createDeployAsync(String org, String area, String product, Component component) {
        return webClient.post()
                .uri("/deploy-api/deploy/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Deploy.class)
                .doOnSuccess(response -> logger.info("Deploy vertical created"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

    @Override
    public Mono<DeleteGithubFileRequest> deleteDeployAsync(String org, String area, String product, String name,
            String enviroment) {
        return webClient
                .delete()
                .uri("/deploy-api/deploy/" + org + "/" + area + "/" + product + "/" + name + "/" + enviroment)
                .retrieve()
                .bodyToMono(DeleteGithubFileRequest.class)
                .doOnSuccess(response -> logger.info("Deploy vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<Deploy> updateDeployAsync(String org, String area, String product, Component component) {

        return webClient
                .put()
                .uri("/deploy-api/deploy/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Deploy.class)
                .doOnSuccess(response -> logger.info("Deploy vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

}
