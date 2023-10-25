package com.nttdata.knot.baseapi.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Interfaces.IAdministrationService;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.Blueprint;
// import com.nttdata.knot.baseapi.Interfaces.IAzureService;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Area;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Organization;

// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;
// import com.nttdata.knot.baseapi.Models.UserPackage.UserFront;
// import com.nttdata.knot.baseapi.Services.ArgoCDService;
import reactor.core.publisher.Mono;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class AdministrationController {

    private final IAdministrationService administrationService;
    // private final ArgoCDService argoCDService;
    // private final IAzureService azureService;


    @Autowired
    public AdministrationController(IAdministrationService administrationService) {
        this.administrationService = administrationService;
        // this.argoCDService = argoCDService;
        // this.azureService= azureService;

    }

    @GetMapping("/organization")
    public ResponseEntity<Mono<List<Object>>> getOrganizationList() throws JsonProcessingException {      
        var organizationList = this.administrationService.getOrganizationList();
        return ResponseEntity.ok(organizationList);
    }

    @GetMapping("/organization/{id}")
    public ResponseEntity<Mono<Organization>> getOrganizationByName(@PathVariable String id) throws JsonProcessingException {      
        var organization = this.administrationService.getOrganizationByName(id);
        return ResponseEntity.ok(organization);
    }

    @PostMapping("/organization")
    public ResponseEntity<Mono<Boolean>> createOrganization(@RequestBody Organization organization) throws JsonProcessingException {      
        var canCreateNewOrganization = this.administrationService.createOrganization(organization);
        return ResponseEntity.ok(canCreateNewOrganization);
    }

    @PutMapping("/organization")
    public ResponseEntity<Mono<Organization>> updateOrganization(@RequestBody Organization organization) throws JsonProcessingException {
        var updatedOrganization = this.administrationService.updatedOrganization(organization);
        return ResponseEntity.ok(updatedOrganization);
    }

    @DeleteMapping("/organization/{id}")
    public ResponseEntity<Mono<Organization>> deleteOrganization(@PathVariable String id) throws JsonProcessingException {      
        var newOrganization = this.administrationService.deleteOrganization(id);
        return ResponseEntity.ok(newOrganization);
    }

    @GetMapping("/organization/{idOrganization}/areas")
    public ResponseEntity<Mono<List<Area>>> getAreaList(@PathVariable String idOrganization) throws JsonProcessingException {
        var areaList = this.administrationService.getAreaList(idOrganization);
        return ResponseEntity.ok(areaList);
    }

    @PostMapping("/organization/{idOrganization}/areas")
    public ResponseEntity<Mono<Boolean>> createArea(@PathVariable String idOrganization, @RequestBody Area area) throws JsonProcessingException {
        var canCreateNewArea = this.administrationService.createArea(area, idOrganization);
        return ResponseEntity.ok(canCreateNewArea);
    }

    @PutMapping("/organization/{idOrganization}/areas")
    public ResponseEntity<Mono<Area>> updateArea(@PathVariable String idOrganization, @RequestBody Area area) throws JsonProcessingException {
        var updatedArea = this.administrationService.updatedArea(area, idOrganization);
        return ResponseEntity.ok(updatedArea);
    }

    @DeleteMapping("/organization/{idOrganization}/areas/{idArea}")
    public ResponseEntity<Mono<Boolean>> deleteArea(@PathVariable String idOrganization, @PathVariable String idArea) throws JsonProcessingException {
        var canDeleteArea = this.administrationService.deleteArea(idOrganization, idArea);
        return ResponseEntity.ok(canDeleteArea);
    }

    @GetMapping("/blueprint/{idOrganization}/{idArea}")
    public ResponseEntity<Mono<List<Blueprint>>> getAreaList(@PathVariable String idOrganization, @PathVariable String idArea) throws JsonProcessingException {
        var blueprintList = this.administrationService.getBlueprintList(idOrganization, idArea);
        return ResponseEntity.ok(blueprintList);
    }

    @PostMapping("/blueprint/{idOrganization}/{idArea}")
    public ResponseEntity<Mono<Boolean>> createBlueprint(@RequestBody Blueprint blueprint, @PathVariable String idOrganization, @PathVariable String idArea) throws JsonProcessingException {
        var canCreateNewBlueprint = this.administrationService.createBlueprint(blueprint, idOrganization, idArea);
        return ResponseEntity.ok(canCreateNewBlueprint);
    }

    @PutMapping("/blueprint/{idOrganization}/{idArea}")
    public ResponseEntity<Mono<Blueprint>> updateBlueprint(@RequestBody Blueprint blueprint, @PathVariable String idOrganization, @PathVariable String idArea) throws JsonProcessingException {
        var updatedBlueprint = this.administrationService.updatedBlueprint(blueprint, idOrganization, idArea);
        return ResponseEntity.ok(updatedBlueprint);
    }

    @DeleteMapping("/blueprint/{idOrganization}/{idArea}/{idBlueprint}")
    public ResponseEntity<Mono<Blueprint>> deleteBlueprint(@PathVariable String idOrganization, @PathVariable String idArea, @PathVariable String idBlueprint) throws JsonProcessingException {
        var newBlueprint = this.administrationService.deleteBlueprint(idOrganization, idArea, idBlueprint);
        return ResponseEntity.ok(newBlueprint);
    }

    @GetMapping("/config")
    public ResponseEntity<Mono<Object>> getConfig() throws JsonProcessingException {      
        var config = this.administrationService.getConfig();
        return ResponseEntity.ok(config);
    }

    @GetMapping("/catalog")
    public ResponseEntity<Mono<List<Object>>> getCatalog() throws JsonProcessingException {      
        var catalog = this.administrationService.getCatalog();
        return ResponseEntity.ok(catalog);
    }

}
//     @GetMapping("/test")
//     public ResponseEntity<Mono<String>> test() throws SSLException {
//         var userList = argoCDService.getArgoCDApplicationAsync("knot-api");
//         return ResponseEntity.ok(userList);
//     }
   
//     @PostMapping("/test2")
//     public ResponseEntity<Mono<ArgoCdApplication>> test(@RequestBody ArgoCdApplication argoCDApplication) throws SSLException {
//         var userList = argoCDService.createArgoCDApplicationAsync(argoCDApplication);
//         return ResponseEntity.ok(userList);
//     }

//     @PostMapping("/test3")
//     public ResponseEntity<Mono<Void>> test1() throws SSLException {
//         var userList = azureService.createSecretAsync("github-knot-kv", "base-api", "123456");
//         return ResponseEntity.ok(userList);
//     }
// }
