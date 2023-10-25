package com.nttdata.knot.baseapi.Interfaces;

import java.util.List;

import com.nttdata.knot.baseapi.Models.GithubPackage.GithubBranch;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubCommits.GetGithubCommitsResponse;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileResponse.GetGithubFileResponse;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubTag.GetGithubTagResponse;
import com.nttdata.knot.baseapi.Models.UserPackage.GetUser;

import reactor.core.publisher.Mono;

public interface IGithubService {
    Mono<GetGithubFileResponse> getGithubFileAsync(String repoName, String name);

    Mono<String> createGithubFileAsync(CreateGithubFileRequest createGithubFile, String repoName, String name);

    Mono<String> deleteGithubFileAsync(DeleteGithubFileRequest deleteGithubFile, String repoName, String name);

    Mono<String> createGithubTagAsync(String repoName, String source, String target, String message);

    Mono<List<GetGithubCommitsResponse>> getGithubCommitsAsync(String repoName);

    Mono<List<GetGithubTagResponse>> getGithubTagAsync(String repoName);

    Mono<List<GetUser>> getGithubUserList();

    Mono<List<String>> listDevContainers();

    Mono<List<GithubBranch>> getGithubBranches(String repoName, String userToken);
}
