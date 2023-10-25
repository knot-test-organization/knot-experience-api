package com.nttdata.knot.baseapi.Interfaces.Old_Interfaces;


import reactor.core.publisher.Mono;

public interface IKubernetesService {
    Mono<Void> createConfigMap(String githubToken, String username);
}

