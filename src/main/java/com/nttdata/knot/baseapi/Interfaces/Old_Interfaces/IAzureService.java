package com.nttdata.knot.baseapi.Interfaces.Old_Interfaces;


import reactor.core.publisher.Mono;

public interface IAzureService {
    Mono<String> createSecretAsync(String keyVaultName, String secretName, String secretValue);
    public Mono<String> deleteSecretAsync(String keyVaultName, String secretName);
}