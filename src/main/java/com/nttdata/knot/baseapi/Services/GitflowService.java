package com.nttdata.knot.baseapi.Services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nttdata.knot.baseapi.Interfaces.IAlmService;
import com.nttdata.knot.baseapi.Interfaces.IComponentService;
import com.nttdata.knot.baseapi.Interfaces.IGitflowService;
import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.GitflowPackage.ComponentRelease;
import com.nttdata.knot.baseapi.Models.GitflowPackage.Release;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import reactor.core.publisher.Mono;

@Service
public class GitflowService implements IGitflowService {

    private final IGithubService githubService;
    private final IComponentService componentService;
    private final IAlmService almService;

    public GitflowService(IGithubService githubService, IComponentService componentService, IAlmService almService) {
        this.githubService = githubService;
        this.componentService = componentService;
        this.almService = almService;
    }

    @Override
    public Mono<List<Release>> listReleases(String org, String area, String product, String id) {
        Component component = this.componentService.getComponentByNameAsync(org, area, product, id).block();

        List<Release> listReleases = this.almService.listReleases(component).block();
        
        return Mono.just(listReleases);
    }

    @Override
    public Mono<Release> createRelease(String org, String area, String product, String id, Release release) {
        Component component = this.componentService.getComponentByNameAsync(org, area, product, id).block();
        
        ComponentRelease componentRelease = new ComponentRelease(component, release);

        Release createRelease = this.almService.createRelease(componentRelease).block();

        return Mono.just(createRelease);
    }

    @Override
    public Mono<Release> actionRelease(String org, String area, String product, String id, String type,
            Release release) {
        Component component = this.componentService.getComponentByNameAsync(org, area, product, id).block();

        ComponentRelease componentRelease = new ComponentRelease(component, release);

        Release actionRelease = new Release();

        switch (type) {
            case "promote_pro":
                actionRelease = this.almService.promoteRelease(componentRelease).block();
                break;

            case "approve_pro":
                actionRelease = this.almService.approveProRelease(componentRelease).block();
                break;

            case "approve_pre":
                actionRelease = this.almService.approvePreRelease(componentRelease).block();
                break;
        }

        return Mono.just(actionRelease);
    }
    
}
