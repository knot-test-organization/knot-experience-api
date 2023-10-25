package com.nttdata.knot.baseapi.Interfaces;

import java.util.List;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GitflowPackage.ComponentRelease;
import com.nttdata.knot.baseapi.Models.GitflowPackage.Release;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.ALM.ALM;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineResponseStatus;

import reactor.core.publisher.Mono;

public interface IAlmService {

    Mono<ALM> createAlmAsync(String org, String area, String product, Component component);

    Mono<DeleteGithubFileRequest> deleteAlmAsync(String org, String area, String product, String name);

    Mono<ALM> updateAlmAsync(String org, String area, String product, Component component);

    Mono<List<PipelineResponseStatus>> getPipelineInfoAsync(PipelineRequestStatus pipelineRequestStatus);

    Mono<List<String>> listPipelineExecutionsAsync(String pipelineName);

    Mono<List<String>> listPipelineStepsAsync(String executionName);

    Mono<List<PipelineResponseStatus>> getInitialTableAsync(String componentName);

    Mono<List<Release>> listReleases(Component component);

    Mono<Release> createRelease(ComponentRelease componentRelease);

    Mono<Release> promoteRelease(ComponentRelease componentRelease);

    Mono<Release> approvePreRelease(ComponentRelease componentRelease);

    Mono<Release> approveProRelease(ComponentRelease componentRelease);

}
