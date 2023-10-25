package com.nttdata.knot.baseapi.Services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.nttdata.knot.baseapi.Interfaces.IAlmService;
import com.nttdata.knot.baseapi.Interfaces.IArgoCDService;
import com.nttdata.knot.baseapi.Interfaces.ICodeService;
import com.nttdata.knot.baseapi.Interfaces.ICollaborationService;
import com.nttdata.knot.baseapi.Interfaces.IComponentService;
import com.nttdata.knot.baseapi.Interfaces.IDeployService;
import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Interfaces.IIacService;
import com.nttdata.knot.baseapi.Interfaces.Old_Interfaces.IAzureService;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Metadata;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Label;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.*;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Metadata;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ApplicationDestination;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ApplicationSpec;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Automated;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Helm;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Label;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Source;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.SyncPolicy;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.Project;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ProjectDestination;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ProjectSpec;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.Blueprint;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintConfig;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintFront;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.ComponentPackage.ComponentList;
// import com.nttdata.knot.baseapi.Models.ComponentPackage.Env;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubCommits.GetGithubCommitsResponse;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.Committer;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubTag.GetGithubTagResponse;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.OnboardingTemplate;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.ALM.ALM;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Code;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Code.Codespace;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Collaboration.Collaboration;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Deployment.Deploy;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.Enviroments.Envs;
import com.nttdata.knot.baseapi.Models.OnboardingTemplatePackage.IaC.IaC;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.baseapi.Models.PipelineStatusPackage.PipelineResponseStatus;
import com.nttdata.knot.baseapi.Models.ProductPackage.Product;
import com.nttdata.knot.baseapi.Models.ProductPackage.ProductList;
import com.nttdata.knot.baseapi.Models.UserPackage.User;

import reactor.core.publisher.Mono;

@Service
public class ComponentService implements IComponentService {

    private final Logger logger = LoggerFactory.getLogger(ComponentService.class);
    YAMLFactory yamlFactory;
    ObjectMapper objectMapper;
    private final IGithubService githubService;
    private final IArgoCDService argoCDService;
    // private final IKubernetesService kubernetesService;
    private final IAzureService azureService;
    private final IAlmService almService;
    private final IDeployService deployService;
    private final ICodeService codeService;
    private final IIacService iacService;
    private final ICollaborationService collaborationService;

    public ComponentService(IGithubService githubService, IArgoCDService argoCDService,
            IAzureService azureService, IAlmService almService, IDeployService deployService,
            ICodeService codeService, IIacService iacService, ICollaborationService collaborationService) {

        this.yamlFactory = new YAMLFactory();
        this.yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        this.objectMapper = new ObjectMapper(yamlFactory);
        this.githubService = githubService;
        this.argoCDService = argoCDService;
        this.azureService = azureService;
        this.almService = almService;
        this.deployService = deployService;
        this.codeService = codeService;
        this.iacService = iacService;
        this.collaborationService = collaborationService;
    }

    public Mono<Component> getComponentByNameAsync(String org, String area, String product, String id) {
        String repoName = "knot-onboarding-resources";
        String path = "products/" + org + "/" + area + "/" + product + "/" + id + "/values.yaml";

        var valuesFile = this.githubService.getGithubFileAsync(repoName, path).block();

        String base64String = valuesFile.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            // ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            Component existingComponent = objectMapper.readValue(
                    contentAsString,
                    Component.class);

            return Mono.just(existingComponent);
        } catch (JsonProcessingException e) {
            return Mono.empty();
        }
    }

    @Override
    public Mono<Component> createComponentAsync(String org, String area, String product, Component component,
            String openapifileyaml)
            throws JsonProcessingException {

        String repoName = "knot-onboarding-resources";
        String valuesFilePath = "products/product.yaml";
        component.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Build argo project object
        // ArgoCdProject createArgoCdProject = InitializeArgoCdProject(component);

        // Create project in ArgoCD
        // this.argoCDService.createArgoCDProjectAsync(createArgoCdProject).block();

        // add component to the general values with the list of components

        var existingProductRefList = this.githubService.getGithubFileAsync(repoName, valuesFilePath).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            Product deleteProduct = new Product();
            Product newProduct = new Product();

            for (Product prod : existingProductList.getProducts()) {
                if (prod.getOrganization().equals(org) && prod.getArea().equals(area) && prod.getId().equals(product)) {
                    deleteProduct = prod;
                    newProduct = prod;
                    newProduct.getComponents().add(component.getId());
                }
            }

            existingProductList.getProducts().remove(deleteProduct);
            existingProductList.getProducts().add(newProduct);

            String contentInBase64String = Base64.getEncoder()
                    .encodeToString(this.objectMapper
                            .writeValueAsString(existingProductList)
                            .getBytes(StandardCharsets.UTF_8));

            CreateGithubFileRequest createValuesFileRequest = createGithubFileRequest(component,
                    contentInBase64String);
            createValuesFileRequest.setSha(existingProductRefList.getSha());

            // Add the component the values file "list of components"
            this.githubService
                    .createGithubFileAsync(createValuesFileRequest, repoName, valuesFilePath)
                    .block();

            var existingProductRef = this.githubService.getGithubFileAsync(repoName,
                    "products/" + newProduct.getOrganization() + "/" + newProduct.getArea() + "/" + newProduct.getId()
                            + "/values.yaml")
                    .block();

            String contentInBase64StringProduct = Base64.getEncoder()
                    .encodeToString(this.objectMapper
                            .writeValueAsString(newProduct)
                            .getBytes(StandardCharsets.UTF_8));

            updateGithubFileRequest(contentInBase64StringProduct,
                    existingProductRef.getSha(),
                    "Add Component with name " + component.getName() + " into the " + newProduct.getName()
                            + " product",
                    repoName,
                    "products/" + newProduct.getOrganization() + "/" + newProduct.getArea() + "/" + newProduct.getId()
                            + "/values.yaml");

            // save a generic openapi yaml file in the repository if user doesn't provide
            // one
            if (component.getTechnology().equals("springboot-restservice")
                    || component.getTechnology().equals("java-restclient")) {

                String yamlfile = "";
                if (!openapifileyaml.isEmpty()) {
                    yamlfile = openapifileyaml;
                } else {
                    String openApiFilePath = "specification/src/main/resources/openapi.yml";
                    var existingOpenApiFileValuesFile = this.githubService
                            .getGithubFileAsync("knot-springboot-restservice-template",
                                    openApiFilePath)
                            .block();
                    logger.info("The response from github is " + existingOpenApiFileValuesFile.getName());
                    byte[] existingOpenApiFileValuesFileContent = Base64.getDecoder()
                            .decode(existingOpenApiFileValuesFile.getContent().replaceAll("\\s",
                                    ""));
                    yamlfile = new String(existingOpenApiFileValuesFileContent, StandardCharsets.UTF_8);
                }

                String contentInBase64StringYaml = Base64.getEncoder()
                        .encodeToString(yamlfile.getBytes(StandardCharsets.UTF_8));

                CreateGithubFileRequest createGithubFileRequestYaml = createGithubFileRequest(component,
                        contentInBase64StringYaml);

                // TODO: Change the directory
                this.githubService.createGithubFileAsync(createGithubFileRequestYaml, repoName,
                        "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/openapi.yaml")
                        .block();
            }

            String contentInBase64StringComponent = Base64.getEncoder()
                    .encodeToString(this.objectMapper
                            .writeValueAsString(component)
                            .getBytes(StandardCharsets.UTF_8));

            String filePath = "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/values.yaml";

            createGithubFileRequest(
                    contentInBase64StringComponent,
                    "Add new Component with name " + component.getName() + ", into the " + product + " Product",
                    repoName,
                    filePath);

            // set the onboardingTemplate and make call the each vertical API
            InitializeOnboardingTemplate(org, area, product, component);

            // Create the ArgoCD Application
            // ArgoCdApplication argoCdApplication = createArgoCdApplication(component);

            // create argocd Application
            // this.argoCDService.createArgoCDApplicationAsync(argoCdApplication).block();

            // Refresh the ArgoCD Application
            this.argoCDService.getArgoCDApplicationByRefreshAsync(product).block();

            return Mono.just(component);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Create ArgoCD project
    // private ArgoCdApplication createArgoCdApplication(Component component) {

    // ArgoCdApplication argoCdApplication = new ArgoCdApplication();

    // // list of values paths
    // List<String> valueFiles = new ArrayList<>();

    // // add values paths to the list if vertical is enabled
    // if (component.isAlmEnabled()) {

    // valueFiles.add("products/" + component.getName() + "/values-alm.yaml");

    // } else if (component.isDeployEnabled()) {
    // // add paths of each deployement environment if enabled
    // for (Env environment : component.getEnvironments()) {
    // if (environment.isEnabled()) {
    // valueFiles.add("products/" + component.getName() + "/values-deployment/"
    // + environment.getEnvPath()
    // + "/values-deployment.yaml");
    // }
    // }

    // } else if (component.isCodeEnabled()) {

    // valueFiles.add("products/" + component.getName() + "/values-code.yaml");

    // } else if (component.isIacEnabled()) {

    // // add paths of each deployement environment if enabled
    // for (Env environment : component.getEnvironments()) {
    // if (environment.isEnabled()) {
    // valueFiles.add("products/" + component.getName() + "/values-iac/"
    // + environment.getEnvPath()
    // + "/values-iac.yaml");
    // }
    // }
    // } else if (component.isCollaborationEnabled()) {

    // valueFiles.add("products/" + component.getName() +
    // "/values-collaboration.yaml");

    // }

    // // populate the argo application object
    // Metadata metadata = new Metadata();
    // metadata.setName(component.getName());

    // Label labels = new Label();
    // labels.setTeamLeads(component.getName() + "-leads");
    // labels.setTeamDevelopers(component.getName() + "-developers");

    // metadata.setLabels(labels);
    // argoCdApplication.setMetadata(metadata);

    // ApplicationSpec spec = new ApplicationSpec();

    // ApplicationDestination destination = new ApplicationDestination();
    // destination.setNamespace("gitops");
    // destination.setServer("https://kubernetes.default.svc");

    // spec.setDestination(destination);
    // spec.setProject(component.getName());

    // Source source = new Source();
    // Helm helm = new Helm();

    // // set the argoCD valueFiles with only the enabled verticals values paths
    // helm.setValueFiles(valueFiles);

    // source.setHelm(helm);
    // source.setPath("./");
    // source.setRepoURL("https://github.com/NTTData-HybridCloud/knot-onboarding-resources.git");
    // source.setTargetRevision("HEAD");

    // spec.setSource(source);

    // SyncPolicy syncPolicy = new SyncPolicy();

    // Automated automated = new Automated();
    // automated.setPrune(true);
    // automated.setSelfHeal(true);

    // syncPolicy.setAutomated(automated);

    // List<String> syncOptions = new ArrayList<>();
    // syncOptions.add("ApplyOutOfSyncOnly=true");
    // syncOptions.add("RespectIgnoreDifferences=true");

    // syncPolicy.setSyncOptions(syncOptions);
    // spec.setSyncPolicy(syncPolicy);

    // argoCdApplication.setSpec(spec);

    // return argoCdApplication;
    // }

    // // serialize content of values and prepare a commit
    // private CreateGithubFileRequest prepareValueForCommit(Component component,
    // Object vertical)
    // throws JsonProcessingException {

    // String verticalInBase64String = Base64.getEncoder()
    // .encodeToString(this.objectMapper
    // .writeValueAsString(vertical).getBytes(StandardCharsets.UTF_8));

    // Committer committer = new Committer();
    // committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
    // committer.setName("github-actions[bot]");

    // CreateGithubFileRequest createGithubFileRequest = new
    // CreateGithubFileRequest();
    // createGithubFileRequest.setMessage("Add new Component, with name " +
    // component.getName() + " in "
    // + component.getOrganizationName() + " from component list");
    // createGithubFileRequest.setCommitter(committer);
    // createGithubFileRequest.setContent(verticalInBase64String);

    // return createGithubFileRequest;

    // }

    // Create github file request
    private CreateGithubFileRequest createGithubFileRequest(Component component, String contentInBase64String) {
        Committer committer = new Committer();
        committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        committer.setName("github-actions[bot]");

        CreateGithubFileRequest createGithubFileRequest = new CreateGithubFileRequest();
        createGithubFileRequest.setMessage("Add new Component, with name " + component.getName() + " in "
                + component.getOrganizationName() + " from component list");
        createGithubFileRequest.setCommitter(committer);
        createGithubFileRequest.setContent(contentInBase64String);

        return createGithubFileRequest;

    }

    // create a new argoCD project object
    // private ArgoCdProject InitializeArgoCdProject(Component component) {
    // ArgoCdProject argoCdProject = new ArgoCdProject();
    // Project project = new Project();
    // Metadata metadata = new Metadata();
    // metadata.setName(component.getId());
    // Label labels = new Label();
    // labels.setTeamLeads(component.getId() + "-leads");
    // labels.setTeamDevelopers(component.getId() + "-developers");
    // project.setMetadata(metadata);
    // ProjectSpec spec = new ProjectSpec();
    // spec.setDescription("group devops project " + component.getId());
    // List<ProjectDestination> destinations = new ArrayList<>();
    // destinations.add(new ProjectDestination("https://kubernetes.default.svc",
    // "in-cluster", "dev"));
    // destinations.add(new ProjectDestination("https://kubernetes.default.svc",
    // "in-cluster", "pre"));
    // destinations.add(new ProjectDestination("https://kubernetes.default.svc",
    // "in-cluster", "pro"));
    // destinations.add(new ProjectDestination("https://kubernetes.default.svc",
    // "in-cluster", "flux-system"));
    // destinations.add(new ProjectDestination("https://kubernetes.default.svc",
    // "in-cluster", "gitops"));
    // spec.setDestinations(destinations);
    // List<String> sourceRepos = new ArrayList<>();
    // // TODO: remove all references to the organization
    // sourceRepos.add("https://github.com/NTTData-HybridCloud/knot-onboarding-resources.git");
    // sourceRepos.add("https://github.com/NTTData-HybridCloud/knot-generic-templates.git");
    // spec.setSourceRepos(sourceRepos);
    // project.setSpec(spec);
    // argoCdProject.setProject(project);

    // return argoCdProject;
    // }

    // Initialize a new OnboardingTemplate object
    private OnboardingTemplate InitializeOnboardingTemplate(String org, String area, String product,
            Component component) {

        // Initialize the verticals with a null values
        ALM alm = new ALM();
        Deploy deploy = new Deploy();
        Code code = new Code();
        IaC iac = new IaC();
        Collaboration collaboration = new Collaboration();

        // Initialize list of Environments
        List<Envs> environments = component.getEnvironments().stream()
                .map(environment -> new Envs(environment.isEnabled(), environment.getEnvPath(),
                        environment.getNameSpace(), environment.getVersion()))
                .collect(Collectors.toList());

        // make call to the enabled vertical's API to set its values files

        if (component.isAlmEnabled()) {
            alm = this.almService.createAlmAsync(org, area, product, component).block();
        }

        if (component.isDeployEnabled()) {
            deploy = this.deployService.createDeployAsync(org, area, product, component).block();
        }

        if (component.isCodeEnabled()) {
            code = this.codeService.createCodeAsync(org, area, product, component).block();
        }

        if (component.isIacEnabled()) {
            iac = this.iacService.createIacAsync(org, area, product, component).block();
        }

        if (component.isCollaborationEnabled()) {
            collaboration = this.collaborationService.createCollaborationAsync(org, area, product, component).block();
        }

        // populate and return the OnboardingTemplate object

        return new OnboardingTemplate(alm, deploy, code, iac, collaboration, environments);
    }

    @Override
    public Mono<Component> deleteComponentAsync(String org, String area, String product, String id)
            throws JsonProcessingException {
        String repoName = "knot-onboarding-resources";
        // Delete the argoCD Application
        // this.argoCDService.deleteArgoCDApplicationAsync(name).block();

        // Delete ArgoCd project
        // this.argoCDService.deleteArgoCDProjectAsync(name).block();

        // get the general values list
        String productValuesFileName = "products/product.yaml";
        var existingProductValuesFile = this.githubService
                .getGithubFileAsync(repoName, productValuesFileName).block();

        String base64String = existingProductValuesFile.getContent()
                .replaceAll("\\s", "");

        byte[] content = Base64.getDecoder().decode(base64String);
        String contentAsString = new String(content, StandardCharsets.UTF_8);

        ProductList existingProductList = this.objectMapper.readValue(
                contentAsString,
                ProductList.class);

        Product deleteProduct = new Product();
        Product newProduct = new Product();

        for (Product prod : existingProductList.getProducts()) {
            if (prod.getOrganization().equals(org) && prod.getArea().equals(area) && prod.getId().equals(product)) {
                deleteProduct = prod;
                newProduct = prod;
                newProduct.getComponents().remove(id);
            }
        }

        existingProductList.getProducts().remove(deleteProduct);
        existingProductList.getProducts().add(newProduct);

        String contentInBase64String = Base64.getEncoder()
                .encodeToString(this.objectMapper
                        .writeValueAsString(existingProductList)
                        .getBytes(StandardCharsets.UTF_8));

        Component deletedComponent = this.getComponentByNameAsync(org, area, product, id).block();

        updateGithubFileRequest(
                contentInBase64String,
                existingProductValuesFile.getSha(),
                "Removing Component, with name " + deletedComponent.getName() + " to the component list into the "
                        + product + " product",
                repoName,
                productValuesFileName);

        var existingProductRef = this.githubService.getGithubFileAsync(repoName,
                "products/" + newProduct.getOrganization() + "/" + newProduct.getArea() + "/" + newProduct.getId()
                        + "/values.yaml")
                .block();

        String contentInBase64StringProduct = Base64.getEncoder()
                .encodeToString(this.objectMapper
                        .writeValueAsString(newProduct)
                        .getBytes(StandardCharsets.UTF_8));

        updateGithubFileRequest(contentInBase64StringProduct,
                existingProductRef.getSha(),
                "Delete Component with name " + id + " into the " + newProduct.getName()
                        + " product",
                repoName,
                "products/" + newProduct.getOrganization() + "/" + newProduct.getArea() + "/" + newProduct.getId()
                        + "/values.yaml");

        // make call to the enabled vertical's API to delete its values files
        if (deletedComponent.isAlmEnabled()) {
            this.almService.deleteAlmAsync(org, area, product, deletedComponent.getId()).block();
        }

        if (deletedComponent.isDeployEnabled()) {
            // iterate through the different enabled environments
            for (var env : deletedComponent.getEnvironments()) {
                if (env.isEnabled()) {
                    this.deployService
                            .deleteDeployAsync(org, area, product, deletedComponent.getId(), env.getEnvPath())
                            .block();
                }
            }
        }

        if (deletedComponent.isCodeEnabled()) {
            this.codeService.deleteCodeAsync(org, area, product, deletedComponent.getId()).block();
        }

        if (deletedComponent.isIacEnabled()) {
            // iterate through the different enabled environments
            for (var env : deletedComponent.getEnvironments()) {
                if (env.isEnabled()) {
                    this.iacService.deleteIacAsync(org, area, product, deletedComponent.getId(), env.getEnvPath())
                            .block();
                }
            }
        }

        if (deletedComponent.isCollaborationEnabled()) {
            this.collaborationService.deleteCollaborationAsync(org, area, product, deletedComponent.getId()).block();
        }

        // delete openapi swagger file
        try {
            // TODO: Change the directory
            deleteOpenapiFilesAsync(id, "openapi");

        } catch (Exception e) {
            logger.error("OpenApi file does not exist: {}", e.getMessage());
        }

        var valuesFilePath = "products/" + org + "/" + area + "/" + product + "/" + id + "/values.yaml";

        var metadataFile = this.githubService.getGithubFileAsync(repoName, valuesFilePath).block();

        deleteGithubFileRequest(
                metadataFile.getSha(),
                "Removing Component, with name " + deletedComponent.getName() + " into the "
                        + product + " product",
                repoName,
                valuesFilePath);

        // Refresh the ArgoCD Application
        this.argoCDService.getArgoCDApplicationByRefreshAsync(product).block();

        return Mono.just(deletedComponent);
    }

    private void deleteOpenapiFilesAsync(String componenteName, String openapiFileName) {

        var existingOpenApiFile = this.githubService.getGithubFileAsync("knot-onboarding-resources",
                "products/" + componenteName + "/" + openapiFileName + ".yaml").block();
        // create github request
        Committer committer = new Committer();
        committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        committer.setName("github-actions[bot]");

        DeleteGithubFileRequest deleteGithubFileRequest = new DeleteGithubFileRequest();
        deleteGithubFileRequest.setMessage("Delete Swagger OpenApi file from " + componenteName + " Component.");
        deleteGithubFileRequest.setCommitter(committer);
        deleteGithubFileRequest.setSha(existingOpenApiFile.getSha());

        this.githubService.deleteGithubFileAsync(deleteGithubFileRequest, "knot-onboarding-resources",
                "products/" + componenteName + "/" + openapiFileName + ".yaml").block();

    }

    @Override
    public Mono<Component> updateComponentAsync(String org, String area, String product, Component component)
            throws JsonProcessingException {

        String repoName = "knot-onboarding-resources";
        String filePath = "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/values.yaml";
        component.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        var existingComponentValuesFile = this.githubService
                .getGithubFileAsync(repoName, filePath).block();

        String contentInBase64StringComponent = Base64.getEncoder()
                .encodeToString(this.objectMapper
                        .writeValueAsString(component)
                        .getBytes(StandardCharsets.UTF_8));

        updateGithubFileRequest(
                contentInBase64StringComponent,
                existingComponentValuesFile.getSha(),
                "Updating Component, with name " + component.getName() + " into the "
                        + product + " product",
                repoName,
                filePath);

        // set the onboardingTemplate
        updateOnboardingTemplate(org, area, product, component);

        // Create the ArgoCD Application object
        // ArgoCdApplication argoCdApplication = createArgoCdApplication(component);
        // create argocd Application
        // this.argoCDService.updateArgoCDApplicationAsync(argoCdApplication,
        // component.getName()).block();

        return Mono.just(component);
    }

    // Create OnboardingTemplate object
    private OnboardingTemplate updateOnboardingTemplate(String org, String area, String product, Component component) {

        // Initialize the verticals with a null values
        ALM alm = new ALM();
        Deploy deploy = new Deploy();
        Code code = new Code();
        IaC iac = new IaC();
        Collaboration collaboration = new Collaboration();

        // // Initialize list of Environments
        List<Envs> environments = component.getEnvironments().stream()
                .map(environment -> new Envs(environment.isEnabled(), environment.getEnvPath(),
                        environment.getNameSpace(), environment.getVersion()))
                .collect(Collectors.toList());

        // make call to the enabled vertical's API to update its values files

        if (component.isAlmEnabled()) {
            this.almService.updateAlmAsync(org, area, product, component).block();
        }

        if (component.isDeployEnabled()) {
            this.deployService.updateDeployAsync(org, area, product, component).block();

        }

        if (component.isCodeEnabled()) {
            this.codeService.updateCodeAsync(org, area, product, component).block();
        }

        if (component.isIacEnabled()) {
            this.iacService.updateIacAsync(org, area, product, component).block();

        }

        if (component.isCollaborationEnabled()) {
            this.collaborationService.updateCollaborationAsync(org, area, product, component).block();
        }

        // populate the OnboardingTemplate object

        return new OnboardingTemplate(alm, deploy, code, iac, collaboration, environments);
    }

    @Override
    public Mono<Component> promoteComponentAsync(String org, String area, String product, Component component,
            String environment, String source,
            String target) {
        if (!environment.isEmpty() && !source.isEmpty() && !target.isEmpty()) {
            if (environment.equals("pre")) {
                target = target + "-rc";
            }

            var appOfAppSetsValuesFileStatusCode = githubService.createGithubTagAsync(component.getId(),
                    source, target, "Create a new tag for " + component.getName()).block();

            if (!appOfAppSetsValuesFileStatusCode.equals(null)) {
                try {
                    updateComponentAsync(org, area, product, component).block();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return Mono.just(component);
    }

    @Override
    public Mono<Component> createCodespacesAsync(String org, String area, String product, Codespace codespace,
            String token, String repoName,
            String username) throws JsonProcessingException {

        Component component = getComponentByNameAsync(org, area, product, repoName).block();

        // avoid component null warning
        assert component != null;

        // generate a unique secret name
        String secretName = component.getId() + "-" + username.toLowerCase() + "-token";

        // add codespace
        component.getCodespaces().add(codespace);

        // add external name secret
        for (var user : component.getUsers()) {
            if (user.getName().equals(username)) {
                user.setExternalSecretName(secretName);

            }
        }
        // update component
        updateComponentAsync(org, area, product, component).block();

        // create the key vault secret
        this.azureService.createSecretAsync("github-knot-kv", secretName, token).block();

        return Mono.just(component);
    }

    @Override
    public Mono<Component> deleteCodespacesAsync(String org, String area, String product, Codespace codespace,
            String repoName)
            throws JsonProcessingException {

        Component component = getComponentByNameAsync(org, area, product, repoName).block();

        // get the codespace username
        // String username = codespace.getUsername();

        // remove codespace
        assert component != null;
        component.getCodespaces().remove(codespace);

        // generate secret name string
        // String secretName = component.getName() +"-"+ username.toLowerCase() +
        // "-token";

        // delete external name secret from manifest
        // for (var user : component.getUsers()) {
        // if (user.getName().equals(username)) {

        // user.setExternalSecretName(null);

        // }
        // }

        // update application
        updateComponentAsync(org, area, product, component).block();

        // create the key vault secret
        // this.azureService.deleteSecretAsync("github-knot-kv", secretName).block();

        return Mono.just(component);
    }

    @Override
    public Mono<List<String>> listTagsComponentAsync(String repoName, String environment) {
        Mono<List<String>> listTagsMono = Mono.just(new ArrayList<>());

        listTagsMono = listTagsMono.flatMap(existingTags -> {
            if (Objects.equals(environment, "dev")) {
                List<GetGithubCommitsResponse> appOfAppSetsValuesCommits = githubService
                        .getGithubCommitsAsync(repoName).block();

                if (appOfAppSetsValuesCommits.size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        existingTags.add(appOfAppSetsValuesCommits.get(i).getSHA());
                    }
                } else {
                    for (GetGithubCommitsResponse commit : appOfAppSetsValuesCommits) {
                        existingTags.add(commit.getSHA());
                    }
                }
            } else {
                List<GetGithubTagResponse> appOfAppSetsValuesTags = githubService
                        .getGithubTagAsync(repoName).block();

                if (appOfAppSetsValuesTags.size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        if (appOfAppSetsValuesTags.get(i).getName().contains("-rc") || appOfAppSetsValuesTags.get(i).getName().contains("-RC")) {
                            existingTags.add(appOfAppSetsValuesTags.get(i).getName());
                        }
                    }
                } else {
                    for (GetGithubTagResponse tag : appOfAppSetsValuesTags) {
                        if (tag.getName().contains("-rc") || tag.getName().contains("-RC")) {
                            existingTags.add(tag.getName());
                        }
                    }
                }
            }

            return Mono.just(existingTags);
        });

        return listTagsMono;
    }

    @Override
    public Mono<List<BlueprintFront>> listTypesAsync() {
        String repoName = "knot-blueprints-base";
        String fileName = "metadata.yaml";
        var existingTypes = this.githubService.getGithubFileAsync(repoName, fileName).block();

        logger.info("The response from GitHub is " + existingTypes.getName());
        String base64String = existingTypes.getContent().replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            List<Blueprint> typesListResponse = objectMapper.readValue(contentAsString,
                    new TypeReference<List<Blueprint>>() {
                    });

            List<BlueprintFront> blueprintList = new ArrayList<>();

            for (Blueprint blueprint : typesListResponse) {
                BlueprintFront blueprintFront = new BlueprintFront(blueprint.getId(),
                        blueprint.getName());
                blueprintList.add(blueprintFront);
            }

            return Mono.just(blueprintList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<BlueprintConfig> listConfigAsync(String organization, String area, String blueprint) {
        String repoName = "knot-blueprints-base";
        String fileName = organization + "/" + organization + "_" + area + "/blueprint-" + blueprint + ".yaml";
        var existingTypes = this.githubService.getGithubFileAsync(repoName, fileName).block();

        logger.info("The response from GitHub is " + existingTypes.getName());
        String base64String = existingTypes.getContent().replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            BlueprintConfig blueprintConfig = objectMapper.readValue(contentAsString,
                    new TypeReference<BlueprintConfig>() {
                    });

            return Mono.just(blueprintConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Component> scanComponentAsync(String org, String area, String product, String name, boolean active) {
        Component updatedComponent = getComponentByNameAsync(org, area, product, name).block();

        updatedComponent.setSonarqubeScan(active);

        try {
            updateComponentAsync(org, area, product, updatedComponent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Mono.just(updatedComponent);
    }

    @Override
    public Mono<Component> updateUsersComponentAsync(String org, String area, String product, String name,
            List<User> userList) {

        Component updatedComponent = getComponentByNameAsync(org, area, product, name).block();

        updatedComponent.setUsers(userList);

        updatedComponent.getMicrosoftTeams().setUsersList(userList);

        try {
            updateComponentAsync(org, area, product, updatedComponent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Mono.just(updatedComponent);
    }

    @Override
    public Mono<String> downloadOpenapiyaml() {
        String repoName = "knot-springboot-restservice-template";
        String fileName = "specification/src/main/resources/openapi.yml";
        var existingOpenApiFile = this.githubService.getGithubFileAsync(repoName, fileName).block();

        logger.info("The response from GitHub is " + existingOpenApiFile.getName());
        String base64String = existingOpenApiFile.getContent().replaceAll("\\s", "");
        byte[] content = Base64.getDecoder().decode(base64String);
        String contentExistingOpenApiFileAsString = new String(content, StandardCharsets.UTF_8);

        return Mono.just(contentExistingOpenApiFileAsString);
    }

    private Committer setCommitter() {
        Committer committer = new Committer();
        committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        committer.setName("github-actions[bot]");
        return committer;
    }

    private CreateGithubFileRequest createGithubFileRequest(String contentInBase64String, String message,
            String repoName, String filePath) {
        Committer committer = setCommitter();

        CreateGithubFileRequest createGithubFileRequest = new CreateGithubFileRequest();
        createGithubFileRequest
                .setMessage(message);
        createGithubFileRequest.setCommitter(committer);
        createGithubFileRequest.setContent(contentInBase64String);

        this.githubService.createGithubFileAsync(createGithubFileRequest,
                repoName,
                filePath)
                .block();

        return createGithubFileRequest;
    }

    private CreateGithubFileRequest updateGithubFileRequest(String contentInBase64String, String sha, String message,
            String repoName, String filePath) {
        Committer committer = setCommitter();

        CreateGithubFileRequest updateGithubFileRequest = new CreateGithubFileRequest();
        updateGithubFileRequest
                .setMessage(message);
        updateGithubFileRequest.setCommitter(committer);
        updateGithubFileRequest.setContent(contentInBase64String);
        updateGithubFileRequest.setSha(sha);

        this.githubService.createGithubFileAsync(updateGithubFileRequest,
                repoName,
                filePath)
                .block();

        return updateGithubFileRequest;
    }

    private DeleteGithubFileRequest deleteGithubFileRequest(String sha, String message, String repoName,
            String filePath) {
        Committer committer = setCommitter();

        DeleteGithubFileRequest deleteGithubFileRequest = new DeleteGithubFileRequest();
        deleteGithubFileRequest
                .setMessage(message);
        deleteGithubFileRequest.setCommitter(committer);
        deleteGithubFileRequest.setSha(sha);

        this.githubService.deleteGithubFileAsync(deleteGithubFileRequest,
                repoName,
                filePath)
                .block();

        return deleteGithubFileRequest;
    }

    @Override
    public Mono<List<PipelineResponseStatus>> getPipelineInfoAsync(PipelineRequestStatus pipelineRequestStatus) {
        var results = this.almService.getPipelineInfoAsync(pipelineRequestStatus).block();
        return Mono.just(results);
    }

    @Override
    public Mono<List<String>> listPipelineExecutionsAsync(String pipelineName) {
        var results = this.almService.listPipelineExecutionsAsync(pipelineName).block();
        return Mono.just(results);
    }

    @Override
    public Mono<List<String>> listPipelineStepsAsync(String pipelineName) {
        var results = this.almService.listPipelineStepsAsync(pipelineName).block();
        return Mono.just(results);
    }

    @Override
    public Mono<List<PipelineResponseStatus>> getInitialTableAsync(String componentName) {
        var results = this.almService.getInitialTableAsync(componentName).block();
        return Mono.just(results);
    }
}
