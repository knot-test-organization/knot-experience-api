package com.nttdata.knot.baseapi.Interfaces;


import javax.net.ssl.SSLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;

import reactor.core.publisher.Mono;

public interface IArgoCDService {

    Mono<ArgoCdApplication> createArgoCDApplicationAsync(ArgoCdApplication argoCDApplication);

    Mono<String> deleteArgoCDApplicationAsync(String name);

    Mono<ArgoCdApplication> updateArgoCDApplicationAsync(ArgoCdApplication argoCDApplication, String name);

    Mono<ArgoCdProject> createArgoCDProjectAsync(ArgoCdProject argoCdProject) throws JsonProcessingException ;

    Mono<String> deleteArgoCDProjectAsync(String name);

    Mono<String> getArgoCDApplicationListAsync() throws SSLException ;

    Mono<String> getArgoCDApplicationAsync(String name);

    Mono<ArgoCdApplication> getArgoCDApplicationByRefreshAsync(String name);

}