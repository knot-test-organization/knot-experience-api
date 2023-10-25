package com.nttdata.knot.baseapi.Interfaces;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintConfig;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintFront;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Codespace;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineResponseStatus;
import com.nttdata.knot.baseapi.Models.UserPackage.User;


import reactor.core.publisher.Mono;

import java.util.List;

public interface IComponentService {

    Mono<Component> createComponentAsync(String org, String area, String product, Component component, String openapifile) throws JsonProcessingException;
    Mono<Component> getComponentByNameAsync(String org, String area, String product, String name);
    Mono<Component> deleteComponentAsync(String org, String area, String product, String name) throws JsonProcessingException;
    Mono<Component> updateComponentAsync(String org, String area, String product, Component component)  throws JsonProcessingException;
    Mono<Component> promoteComponentAsync(String org, String area, String product, Component component, String environment, String source, String target);
    Mono<List<String>> listTagsComponentAsync(String repoName, String environment);
    Mono<List<BlueprintFront>> listTypesAsync();
    Mono<BlueprintConfig> listConfigAsync(String organization, String area, String blueprint);
    Mono<Component> scanComponentAsync(String org, String area, String product, String name, boolean active);
    Mono<Component> updateUsersComponentAsync(String org, String area, String product, String name, List<User> userList);
    Mono<Component> createCodespacesAsync(String org, String area, String product, Codespace codespace, String token, String repoName, String username)  throws JsonProcessingException;
    Mono<Component> deleteCodespacesAsync(String org, String area, String product, Codespace codespace, String repoName) throws JsonProcessingException;
    Mono<String> downloadOpenapiyaml();
    Mono<List<PipelineResponseStatus>> getPipelineInfoAsync(PipelineRequestStatus pipelineRequestStatus);
    Mono<List<String>> listPipelineExecutionsAsync(String pipelineName);
    Mono<List<String>> listPipelineStepsAsync(String executionName);
    Mono<List<PipelineResponseStatus>> getInitialTableAsync(String executionName);
}

