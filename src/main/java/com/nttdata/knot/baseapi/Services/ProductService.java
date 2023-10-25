package com.nttdata.knot.baseapi.Services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.nttdata.knot.baseapi.Interfaces.IArgoCDService;
import com.nttdata.knot.baseapi.Interfaces.IComponentService;
import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Interfaces.IProductService;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Metadata;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ApplicationDestination;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ApplicationSpec;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Automated;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Helm;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Label;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Source;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.SyncPolicy;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.Project;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ProjectDestination;
import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ProjectSpec;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.BlueprintFront;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Env;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.Committer;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.baseapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.baseapi.Models.ProductPackage.Product;
import com.nttdata.knot.baseapi.Models.ProductPackage.ProductList;
import com.nttdata.knot.baseapi.Models.ProductPackage.ComponentFilter.EnvironmentWithComponents;
import com.nttdata.knot.baseapi.Models.ProductPackage.ComponentFilter.TypesWithComponents;

import reactor.core.publisher.Mono;

@Service
public class ProductService implements IProductService {

    private IGithubService githubService;
    private final IArgoCDService argoCDService;
    private IComponentService componentService;
    YAMLFactory yamlFactory;
    ObjectMapper objectMapper;
    private final String repoName = "knot-onboarding-resources";

    public ProductService(IGithubService githubService, IArgoCDService argoCDService,
            IComponentService componentService) {
        this.githubService = githubService;
        this.argoCDService = argoCDService;
        this.componentService = componentService;
        this.yamlFactory = new YAMLFactory();
        this.yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        this.objectMapper = new ObjectMapper(yamlFactory);
    }

    @Override
    public Mono<List<Product>> getProductList(String username) {
        List<Product> listProducts = new ArrayList<>();
        String path = "products/product.yaml";

        var existingProductRefList = this.githubService.getGithubFileAsync(this.repoName, path).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            for (Product product : existingProductList.getProducts()) {
                listProducts.add(product);
            }

            return Mono.just(listProducts);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Product> getProductByID(String organization, String area, String id) {
        Product product = new Product();
        String path = "products/product.yaml";

        var existingProductRefList = this.githubService.getGithubFileAsync(this.repoName, path).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            for (Product selectedProduct : existingProductList.getProducts()) {
                if (selectedProduct.getOrganization().equals(organization) && selectedProduct.getArea().equals(area)
                        && selectedProduct.getId().equals(id)) {
                    product = selectedProduct;
                }
            }

            return Mono.just(product);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<List<Component>> getComponentListOfAProduct(String organization, String area, String id) {
        List<Component> listComponents = new ArrayList<>();

        Product product = this.getProductByID(organization, area, id).block();

        for (String componentID : product.getComponents()) {
            String path = "products/" + organization + "/" + area + "/" + id + "/" + componentID + "/values.yaml";
            var existingComponentRefList = this.githubService.getGithubFileAsync(this.repoName, path).block();

            String base64String = existingComponentRefList.getContent()
                    .replaceAll("\\s", "");
            try {
                byte[] content = Base64.getDecoder().decode(base64String);
                String contentAsString = new String(content, StandardCharsets.UTF_8);
                Component existingComponent = this.objectMapper.readValue(
                        contentAsString,
                        Component.class);

                listComponents.add(existingComponent);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return Mono.just(listComponents);
    }

    @Override
    public Mono<List<Object>> getComponentListOfAProductByFilter(String organization, String area, String id,
            String filter) {
        List<Object> list = new ArrayList<>();

        List<Component> listComponents = this.getComponentListOfAProduct(organization, area, id).block();

        switch (filter) {
            case "types":
                List<BlueprintFront> listTypes = this.componentService.listTypesAsync().block();

                Map<String, List<Component>> componentsMap = listComponents.stream()
                        .collect(Collectors.groupingBy(component -> component.getType()));

                List<TypesWithComponents> typesWithComponentsList = listTypes.stream()
                        .map(type -> new TypesWithComponents(type.getId(), type.getLabel(),
                                componentsMap.get(type.getId())))
                        .collect(Collectors.toList());

                list.addAll(typesWithComponentsList);
                break;

            case "environments":

                Product product = this.getProductByID(organization, area, id).block();

                List<EnvironmentWithComponents> envsWithComponentsList = new ArrayList<>();

                for (Env environment : product.getEnvironments()) {
                    List<Component> componentList = listComponents.stream()
                            .filter(component -> component.getEnvironments().stream()
                                    .anyMatch(env -> env.getEnvPath().equals(environment.getEnvPath())
                                            && env.isEnabled()))
                            .collect(Collectors.toList());

                    EnvironmentWithComponents envWithComponents = new EnvironmentWithComponents(
                            environment.isEnabled(),
                            environment.getEnvPath(),
                            environment.getNameSpace(),
                            environment.getVersion(),
                            componentList);

                    envsWithComponentsList.add(envWithComponents);
                }

                list.addAll(envsWithComponentsList);
                break;
        }

        return Mono.just(list);
    }

    @Override
    public Mono<Boolean> createProductAsync(Product product) throws JsonProcessingException {
        String path = "products/product.yaml";

        var existingProductRefList = this.githubService.getGithubFileAsync(this.repoName, path).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            Boolean canAddProduct = true;

            for (Product prod : existingProductList.getProducts()) {

                if (prod.getId().equals(product.getId()) &&
                        prod.getOrganization().equals(product.getOrganization()) &&
                        prod.getArea().equals(product.getArea())) {
                    canAddProduct = false;
                }

            }
            if (canAddProduct) {
                // Build argo project object
                ArgoCdProject createArgoCdProject = InitializeArgoCdProject(product);

                // Create project in ArgoCD
                this.argoCDService.createArgoCDProjectAsync(createArgoCdProject).block();

                product.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                existingProductList.getProducts().add(product);

                String contentInBase64String = Base64.getEncoder()
                        .encodeToString(this.objectMapper
                                .writeValueAsString(existingProductList)
                                .getBytes(StandardCharsets.UTF_8));

                updateGithubFileRequest(contentInBase64String,
                        existingProductRefList.getSha(),
                        "Create Product with name " + product.getName(),
                        this.repoName,
                        path);

                String contentInBase64StringProduct = Base64.getEncoder()
                        .encodeToString(this.objectMapper
                                .writeValueAsString(product)
                                .getBytes(StandardCharsets.UTF_8));

                createGithubFileRequest(contentInBase64StringProduct,
                        "Create Product values with name " + product.getName() + " into the "
                                + product.getOrganization() + " organization",
                        this.repoName,
                        "products/" + product.getOrganization() + "/" + product.getArea() + "/" + product.getId()
                                + "/values.yaml");

                // Create the ArgoCD Application
                ArgoCdApplication argoCdApplication = createArgoCdApplication(product);

                // create argocd Application
                this.argoCDService.createArgoCDApplicationAsync(argoCdApplication).block();
            }

            return Mono.just(canAddProduct);

        } catch (JsonProcessingException e) {
            return Mono.just(false);
        }

    }

    // create a new argoCD project object
    private ArgoCdProject InitializeArgoCdProject(Product product) {

        ArgoCdProject argoCdProject = new ArgoCdProject();
        Project project = new Project();
        Metadata metadata = new Metadata();
        metadata.setName(product.getId());
        Label labels = new Label();
        labels.setTeamLeads(product.getId() + "-leads");
        labels.setTeamDevelopers(product.getId() + "-developers");
        project.setMetadata(metadata);
        ProjectSpec spec = new ProjectSpec();
        spec.setDescription("group devops project " + product.getId());
        List<ProjectDestination> destinations = new ArrayList<>();
        destinations.add(new ProjectDestination("https://kubernetes.default.svc", "in-cluster", "dev"));
        destinations.add(new ProjectDestination("https://kubernetes.default.svc", "in-cluster", "pre"));
        destinations.add(new ProjectDestination("https://kubernetes.default.svc", "in-cluster", "pro"));
        destinations.add(new ProjectDestination("https://kubernetes.default.svc", "in-cluster", "flux-system"));
        destinations.add(new ProjectDestination("https://kubernetes.default.svc", "in-cluster", "gitops"));
        spec.setDestinations(destinations);
        List<String> sourceRepos = new ArrayList<>();
        // TODO: remove all references to the organization
        sourceRepos.add("https://github.com/NTTData-HybridCloud/knot-onboarding-resources.git");
        sourceRepos.add("https://github.com/NTTData-HybridCloud/knot-generic-templates.git");
        spec.setSourceRepos(sourceRepos);
        project.setSpec(spec);
        argoCdProject.setProject(project);

        return argoCdProject;
    }

    private ArgoCdApplication createArgoCdApplication(Product product) {

        ArgoCdApplication argoCdApplication = new ArgoCdApplication();

        // list of values paths
        List<String> valueFiles = new ArrayList<>();

        valueFiles.add("$values/products/" + product.getOrganization() + "/" + product.getArea() + "/" + product.getId()
                + "/values.yaml");

        // populate the argo application object
        Metadata metadata = new Metadata();
        metadata.setName(product.getId());

        Label labels = new Label();
        labels.setTeamLeads(product.getId() + "-leads");
        labels.setTeamDevelopers(product.getId() + "-developers");

        metadata.setLabels(labels);
        argoCdApplication.setMetadata(metadata);

        ApplicationSpec spec = new ApplicationSpec();

        ApplicationDestination destination = new ApplicationDestination();
        destination.setNamespace("gitops");
        destination.setServer("https://kubernetes.default.svc");

        spec.setDestination(destination);
        spec.setProject(product.getId());

        List<Source> sources = new ArrayList<>();
        Source sourceGeneric = new Source();
        Source sourceOnboarding = new Source();
        Helm helm = new Helm();

        // set the argoCD valueFiles with only the enabled verticals values paths
        helm.setValueFiles(valueFiles);

        sourceGeneric.setHelm(helm);
        sourceGeneric.setPath("onboarding-product");
        sourceGeneric.setRepoURL("https://github.com/NTTData-HybridCloud/knot-generic-templates.git");
        sourceGeneric.setTargetRevision("HEAD");

        sourceOnboarding.setRepoURL("https://github.com/NTTData-HybridCloud/knot-onboarding-resources.git");
        sourceOnboarding.setTargetRevision("HEAD");
        sourceOnboarding.setRef("values");

        sources.add(sourceGeneric);
        sources.add(sourceOnboarding);

        spec.setSources(sources);

        SyncPolicy syncPolicy = new SyncPolicy();

        Automated automated = new Automated();
        automated.setPrune(true);
        automated.setSelfHeal(true);

        syncPolicy.setAutomated(automated);

        List<String> syncOptions = new ArrayList<>();
        syncOptions.add("ApplyOutOfSyncOnly=true");
        syncOptions.add("RespectIgnoreDifferences=true");

        syncPolicy.setSyncOptions(syncOptions);
        spec.setSyncPolicy(syncPolicy);

        argoCdApplication.setSpec(spec);

        return argoCdApplication;
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
    public Mono<Boolean> updateProductAsync(Product product) throws JsonProcessingException {

        String filePath = "products/" + product.getOrganization() + "/" + product.getArea() + "/" + product.getId()
                + "/values.yaml";

        var existingProductValuesFile = this.githubService
                .getGithubFileAsync(this.repoName, filePath).block();

        String contentInBase64StringProduct = Base64.getEncoder()
                .encodeToString(this.objectMapper
                        .writeValueAsString(product)
                        .getBytes(StandardCharsets.UTF_8));

        String path = "products/product.yaml";
        var existingProductRefList = this.githubService.getGithubFileAsync(this.repoName, path).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            Boolean canUpdateProduct = false;
            int index = 0;

            for (int i = 0; i < existingProductList.getProducts().size(); i++) {
                if (existingProductList.getProducts().get(i).getId().equals(product.getId())) {
                    index = i;
                    canUpdateProduct = true;
                }

            }

            if (canUpdateProduct) {
                // product.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy
                // HH:mm")));

                existingProductList.getProducts().set(index, product);

                String contentInBase64String = Base64.getEncoder()
                        .encodeToString(this.objectMapper
                                .writeValueAsString(existingProductList)
                                .getBytes(StandardCharsets.UTF_8));

                updateGithubFileRequest(contentInBase64String,
                        existingProductRefList.getSha(),
                        "Update Product with name " + product.getName(),
                        this.repoName,
                        path);

                updateGithubFileRequest(
                        contentInBase64StringProduct,
                        existingProductValuesFile.getSha(),
                        "Updating Product, with name " + product.getName(),
                        repoName,
                        filePath);
            }

            return Mono.just(canUpdateProduct);

        } catch (JsonProcessingException e) {
            return Mono.just(false);
        }
    }

    @Override
    public Mono<Boolean> deleteProductAsync(String organization, String area, String id)
            throws JsonProcessingException {

        Product deleteProduct = this.getProductByID(organization, area, id).block();

        String filePath = "products/" + deleteProduct.getOrganization() + "/" + deleteProduct.getArea() + "/"
                + deleteProduct.getId()
                + "/values.yaml";

        String listPath = "products/product.yaml";

        var existingProductRefList = this.githubService.getGithubFileAsync(this.repoName, listPath).block();

        String base64String = existingProductRefList.getContent()
                .replaceAll("\\s", "");
        try {
            // REMOVE PRODUCT FROM PRODUCT LIST
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ProductList existingProductList = this.objectMapper.readValue(
                    contentAsString,
                    ProductList.class);

            for (Product p : existingProductList.getProducts()) {
                if (p.getOrganization().equals(organization) && p.getArea().equals(area) && p.getId().equals(id)) {
                    deleteProduct = p;
                }
            }

            this.argoCDService.deleteArgoCDApplicationAsync(deleteProduct.getId()).block();

            for (String component : deleteProduct.getComponents()) {
                this.componentService.deleteComponentAsync(organization, area, id, component);
            }

            existingProductList.getProducts().remove(deleteProduct);

            String contentInBase64String = Base64.getEncoder()
                    .encodeToString(this.objectMapper
                            .writeValueAsString(existingProductList)
                            .getBytes(StandardCharsets.UTF_8));

            updateGithubFileRequest(contentInBase64String,
                    existingProductRefList.getSha(),
                    "Delete Product with name " + deleteProduct.getName(),
                    this.repoName,
                    listPath);

            // REMOVE PRODUCT FROM values.yaml

            var existingProductValuesFile = this.githubService
                    .getGithubFileAsync(this.repoName, filePath).block();

            deleteGithubFileRequest(
                    existingProductValuesFile.getSha(),
                    "Deleting Product, with name " + deleteProduct.getName(),
                    this.repoName,
                    filePath);

            return Mono.just(true);

        } catch (JsonProcessingException e) {
            return Mono.just(false);
        }

    }

}
