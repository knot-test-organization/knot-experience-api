package com.nttdata.knot.baseapi.Services;

import com.azure.security.keyvault.secrets.SecretAsyncClient;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.nttdata.knot.baseapi.Interfaces.Old_Interfaces.IAzureService;

import reactor.core.publisher.Mono;

@Service
public class AzureService implements IAzureService {

   // private static final Logger logger = LoggerFactory.getLogger(AzureService.class);

    @Override
    public Mono<String> createSecretAsync(String keyVaultName, String secretName, String secretValue) {
        String kvUri = "https://" + keyVaultName + ".vault.azure.net";
        DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();
        SecretAsyncClient client = new SecretClientBuilder()
                .vaultUrl(kvUri)
                .credential(credential)
                .buildAsyncClient();
        // Create a new secret
        return client.setSecret(secretName, secretValue)
                .map(secret -> "Secret created successfully: " + secret.getName())
                .onErrorResume(e -> Mono.just("Failed to create secret: " + e.getMessage()));
    }

    @Override
    public Mono<String> deleteSecretAsync(String keyVaultName, String secretName) {
        String kvUri = "https://" + keyVaultName + ".vault.azure.net";
        DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();
        SecretAsyncClient client = new SecretClientBuilder()
                                              .vaultUrl(kvUri).credential(credential).buildAsyncClient();

        // Delete the secret
        return client.beginDeleteSecret(secretName)
                .last()
                .map(pollResponse -> "Secret deletion started: " + pollResponse.getValue().getName())
                .onErrorResume(e -> Mono.just("Failed to start secret deletion: " + e.getMessage()));
    }
}
