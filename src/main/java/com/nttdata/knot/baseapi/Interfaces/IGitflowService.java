package com.nttdata.knot.baseapi.Interfaces;

import java.util.List;

import com.nttdata.knot.baseapi.Models.GitflowPackage.Release;

import reactor.core.publisher.Mono;

public interface IGitflowService {
    
    Mono<List<Release>> listReleases(String org, String area, String product, String id);

	Mono<Release> createRelease(String org, String area, String product, String id, Release release);

	Mono<Release> actionRelease(String org, String area, String product, String id, String type, Release release);

}
