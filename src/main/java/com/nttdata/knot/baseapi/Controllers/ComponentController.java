package com.nttdata.knot.baseapi.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Interfaces.IComponentService;
import com.nttdata.knot.baseapi.Interfaces.IGitflowService;
import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintConfig;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintFront;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.ComponentPackage.ComponentOpenApi;
import com.nttdata.knot.baseapi.Models.GitflowPackage.Release;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubBranch;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Codespace;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineResponseStatus;
import com.nttdata.knot.baseapi.Models.UserPackage.User;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/components")
public class ComponentController {

    private static final Logger logger = LoggerFactory.getLogger(ComponentController.class);

    private final IComponentService componentService;
    private final IGithubService githubService;
    private final IGitflowService gitflowService;

    @Autowired
    public ComponentController(IComponentService componentService,
            IGithubService githubService, IGitflowService gitflowService) {
        this.componentService = componentService;
        this.githubService = githubService;
        this.gitflowService = gitflowService;
    }

    @GetMapping("/{org}/{area}/{product}/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> getComponentByName(@PathVariable String org, @PathVariable String area,
            @PathVariable String product,
            @PathVariable String name, Authentication auth) {
        var authorities = auth.getAuthorities();
        logger.info("The teams list:  {} ", authorities);
        var component = componentService.getComponentByNameAsync(org, area, product, name);
        return ResponseEntity.ok(component);
    }

    @PostMapping("/{org}/{area}/{product}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Component>> createApplication(@PathVariable String org, @PathVariable String area,
            @PathVariable String product,
            @RequestBody ComponentOpenApi componentOpenApi, Authentication auth)
            throws JsonProcessingException {

        var componentCreated = componentService.createComponentAsync(org, area, product,
                componentOpenApi.getComponent(), componentOpenApi.getOpenapifile());

        return ResponseEntity.ok(componentCreated);
    }

    @DeleteMapping("/{org}/{area}/{product}/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> deleteComponentByName(@PathVariable String org, @PathVariable String area,
            @PathVariable String product,
            @PathVariable String name, Authentication auth)
            throws JsonProcessingException {

        Mono<Component> componentDeleted = null;

        // Check if the user has the authority to delete the component.
        boolean hasAuthority = false;

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + name + "-leads")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            componentDeleted = componentService.deleteComponentAsync(org, area, product, name);
            logger.info("The component {} is being deleted.", name);
        } else {
            logger.info("You do not have the authority to delete the component '{}'.", name);
        }

        return ResponseEntity.ok(componentDeleted);
    }

    @PutMapping("/{org}/{area}/{product}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> updateApplication(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Component updateComponent,
            Authentication auth)
            throws JsonProcessingException {

        Mono<Component> componentUpdated = null;

        // Check if the user has the authority to update the component.
        boolean hasAuthority = false;
        // var authorities = auth.getAuthorities();
        // logger.info("The teams list: {} ", authorities);

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + updateComponent.getId() + "-leads")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            componentUpdated = componentService.updateComponentAsync(org, area, product, updateComponent);
            logger.info("The component {} is being updated.", updateComponent.getId());
        } else {
            logger.info("You do not have the authority to update the component '{}'.", updateComponent.getName());
        }

        return ResponseEntity.ok(componentUpdated);
    }

    @PutMapping("/{org}/{area}/{product}/promote/{environment}/{source}/{target}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> promoteApplication(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Component promoteComponent,
            @PathVariable String environment,
            @PathVariable String source,
            @PathVariable String target,
            Authentication auth) {

        Mono<Component> componentPromoted = null;

        // Check if the user has the authority to promote the component.
        boolean hasAuthority = false;

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + promoteComponent.getId() + "-leads")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            componentPromoted = componentService.promoteComponentAsync(org, area, product, promoteComponent,
                    environment, source, target);
            logger.info("The component {} is being promoted.", promoteComponent.getId());
        } else {
            logger.info("You do not have the authority to promote the component '{}'.", promoteComponent.getName());
        }

        return ResponseEntity.ok(componentPromoted);
    }

    @GetMapping("/listTags/{repoName}/{environment}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<String>>> listTags(@PathVariable String repoName,
            @PathVariable String environment, Authentication auth) {

        var componentListTags = componentService.listTagsComponentAsync(repoName, environment);
        logger.info("Show the tags list for '{}' in '{}'", environment, repoName);
        return ResponseEntity.ok(componentListTags);
    }

    @GetMapping("/listTypes")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<BlueprintFront>>> listTypes() {
        var applicationListTypes = componentService.listTypesAsync();
        return ResponseEntity.ok(applicationListTypes);
    }

    @GetMapping("/listConfig/{organization}/{area}/{blueprint}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<BlueprintConfig>> listConfig(@PathVariable String organization,
            @PathVariable String area, @PathVariable String blueprint) {
        var applicationListConfig = componentService.listConfigAsync(organization, area, blueprint);
        return ResponseEntity.ok(applicationListConfig);
    }

    @PutMapping("/{org}/{area}/{product}/scan/{name}/{active}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Component>> scanComponent(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @PathVariable String name, @PathVariable boolean active,
            Authentication auth) {

        Mono<Component> applicationUpdated = null;
        // Check if the user has the authority to promote the component.
        boolean hasAuthority = false;

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + name + "-leads")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            applicationUpdated = componentService.scanComponentAsync(org, area, product, name, active);
            logger.info("The application {} is being scanned", name);
        } else {
            logger.info("You do not have the authority to promote the component '{}'.", name);
        }

        return ResponseEntity.ok(applicationUpdated);
    }

    @PutMapping("/{org}/{area}/{product}/users/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> updateUsers(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody List<User> userList, @PathVariable String name,
            Authentication auth)
            throws JsonProcessingException {
        var authorities = auth.getAuthorities();
        logger.info("The teams list:  {} ", authorities);
        Mono<Component> applicationUpdated = null;
        logger.info("team:  {} ", "knot-product-" + name + "-leads");

        // Check if the user has the authority to update the users of the component.
        boolean hasAuthority = false;

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + name + "-leads")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            applicationUpdated = componentService.updateUsersComponentAsync(org, area, product, name, userList);
            logger.info("The component '{}' user's list  has been updated.", name);

        } else {
            logger.info("You do not have the authority to update the component '{}' list.", name);
        }
        return ResponseEntity.ok(applicationUpdated);
    }

    @GetMapping("/listRepoBranches/{repoName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<GithubBranch>>> listRepoBranches(@PathVariable String repoName,
            @RequestHeader("Authorization") String authorizationHeader, Authentication auth) {

        Mono<List<GithubBranch>> branchList = null;
        // Check if the user has the authority to list branches of the component
        // repository.
        boolean hasAuthority = false;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().contains("knot-product-" + repoName + "-leads") ||
                        authority.getAuthority().contains("knot-product-" + repoName + "-developers")) {
                    hasAuthority = true;
                    break;
                }
            }

            if (hasAuthority) {
                String userToken = authorizationHeader.substring("Bearer ".length());
                branchList = githubService.getGithubBranches(repoName, userToken);
                logger.info("The list of available branches in {} repository.", repoName);
            } else {
                logger.info("You do not have the authority to list '{}' repository branches.", repoName);
            }
        }

        return ResponseEntity.ok(branchList);
    }

    @PutMapping("/{org}/{area}/{product}/createCodespaces/{repoName}/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> createCodespaces(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Codespace codespace,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String repoName,
            @PathVariable String username, Authentication auth) throws JsonProcessingException {

        Mono<Component> createCodespace = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // Check if the user has the authority to create codespaces in the component
            // repository.
            boolean hasAuthority = false;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                for (GrantedAuthority authority : auth.getAuthorities()) {
                    if (authority.getAuthority().contains("knot-product-" + repoName + "-leads") ||
                            authority.getAuthority().contains("knot-product-" + repoName + "-developers")) {
                        hasAuthority = true;
                        break;
                    }
                }

                if (hasAuthority) {
                    String token = authorizationHeader.substring("Bearer ".length());
                    createCodespace = componentService.createCodespacesAsync(org, area, product, codespace, token,
                            repoName, username);
                    logger.info("Create a Codespace");
                } else {
                    logger.info("You do not have the authority to create codespaces in '{}' repository ", repoName);
                }
            }

        }

        return ResponseEntity.ok(createCodespace);
    }

    @PutMapping("/{org}/{area}/{product}/deleteCodespaces/{repoName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Component>> deleteCodespaces(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Codespace codespace,
            @PathVariable String repoName, Authentication auth) throws JsonProcessingException {

        Mono<Component> deleteCodespace = null;
        // Check if the user has the authority to delete codespaces in the component
        // repository.
        boolean hasAuthority = false;

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().contains("knot-product-" + repoName + "-leads") ||
                    authority.getAuthority().contains("knot-product-" + repoName + "-developers")) {
                hasAuthority = true;
                break;
            }
        }

        if (hasAuthority) {
            deleteCodespace = componentService.deleteCodespacesAsync(org, area, product, codespace, repoName);
            logger.info("Codespace Deleted.");
        } else {
            logger.info("You do not have the authority to delete codespaces in '{}' repository ", repoName);
        }

        return ResponseEntity.ok(deleteCodespace);
    }

    @GetMapping("/downloadOpenapiyaml")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<String>> downloadOpenapiyaml() {

        var openapiyaml = componentService.downloadOpenapiyaml();

        return ResponseEntity.ok(openapiyaml);
    }

    @PostMapping("/getPipelineInfo")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<PipelineResponseStatus>>> getPipelineInfo(
            @RequestBody PipelineRequestStatus pipelineRequestStatus) {

        var results = componentService.getPipelineInfoAsync(pipelineRequestStatus);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/listPipelineExecutions/{pipelineName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<String>>> listPipelineExecutions(@PathVariable String pipelineName) {

        var results = componentService.listPipelineExecutionsAsync(pipelineName);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/listPipelineSteps/{executionName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<String>>> listPipelineSteps(@PathVariable String executionName) {

        var results = componentService.listPipelineStepsAsync(executionName);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/getInitialTable/{componentName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<PipelineResponseStatus>>> getInitialTable(@PathVariable String componentName) {

        var results = componentService.getInitialTableAsync(componentName);

        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/{org}/{area}/{product}/{id}/releases")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<List<Release>>> listReleases(@PathVariable String org, @PathVariable String area, @PathVariable String product, @PathVariable String id) {

        var listReleases = this.gitflowService.listReleases(org, area, product, id);

        return ResponseEntity.ok(listReleases);
    }

    @PostMapping("/{org}/{area}/{product}/{id}/releases")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Release>> createRelease(@PathVariable String org, @PathVariable String area, @PathVariable String product, @PathVariable String id, @RequestBody Release release) {

        var createRelease = this.gitflowService.createRelease(org, area, product, id, release);

        return ResponseEntity.ok(createRelease);
    }

    @PutMapping("/{org}/{area}/{product}/{id}/releases/action/{type}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Release>> actionRelease(@PathVariable String org, @PathVariable String area, @PathVariable String product, @PathVariable String id, @PathVariable String type, @RequestBody Release release) {

        var actionRelease = this.gitflowService.actionRelease(org, area, product, id, type, release);

        return ResponseEntity.ok(actionRelease);
    }
}
