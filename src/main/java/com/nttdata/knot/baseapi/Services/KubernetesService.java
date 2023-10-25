package com.nttdata.knot.baseapi.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Interfaces.Old_Interfaces.IKubernetesService;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import reactor.core.publisher.Mono;

@Service
public class KubernetesService implements IKubernetesService {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesService.class);
    // private final IGithubService githubService;
    private final KubernetesClient client;

    public KubernetesService(IGithubService githubService) {
        // this.githubService = githubService;
        this.client = new DefaultKubernetesClient();
    }

    @Override
    public Mono<Void> createConfigMap(String githubToken, String username) {
        return Mono.fromRunnable(() -> {
            ConfigMap configMap = new ConfigMapBuilder()
                    .withNewMetadata()
                    .withName(username + "-token")
                    .withNamespace("gitops")
                    .endMetadata()
                    .addToData("githubToken", githubToken)
                    .addToData("username", username)
                    .build();

            try {
                client.configMaps().inNamespace("gitops").create(configMap);
            } catch (Exception e) {
                logger.error("Error creating ConfigMap: " + e.getMessage());
            }
        });
    }
}
