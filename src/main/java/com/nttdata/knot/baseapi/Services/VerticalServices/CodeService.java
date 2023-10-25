package com.nttdata.knot.baseapi.Services.VerticalServices;

import com.nttdata.knot.baseapi.Interfaces.ICodeService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CodeService implements ICodeService {

    private static final Logger logger = LoggerFactory.getLogger(Code.class);
    public WebClient webClient;

    @Autowired
    public CodeService(@Qualifier("apiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Code> createCodeAsync(String org, String area, String product, Component component) {
        return webClient.post()
                .uri("/code-api/code/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Code.class)
                .doOnSuccess(response -> logger.info("Code API response Ok"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));

    }

    @Override
    public Mono<DeleteGithubFileRequest> deleteCodeAsync(String org, String area, String product, String name) {
        return webClient
                .delete()
                .uri("/code-api/code/" + org + "/" + area + "/" + product + "/" + name)
                .retrieve()
                .bodyToMono(DeleteGithubFileRequest.class)
                .doOnSuccess(response -> logger.info("Code vertical deleted"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

     @Override
    public Mono<Code> updateCodeAsync(String org, String area, String product, Component component) {
        return webClient
                .put()
                .uri("/code-api/code/" + org + "/" + area + "/" + product)
                .bodyValue(component)
                .retrieve()
                .bodyToMono(Code.class)
                .doOnSuccess(response -> logger.info("Code vertical updated" ))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }


}
