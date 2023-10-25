package com.nttdata.knot.baseapi.Controllers;

import com.nttdata.knot.baseapi.Interfaces.Old_Interfaces.IUserService;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;
import com.nttdata.knot.baseapi.Models.UserPackage.UserFront;
// import com.nttdata.knot.baseapi.Services.ArgoCDService;
//import com.nttdata.knot.baseapi.Services.AzureService;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final IUserService userService;
    // private final AzureService azureService;
    // private final ArgoCDService argoCDService;
    // private final IAzureService azureService;

    @Autowired
    public UsersController(IUserService userService) {
        this.userService = userService;
        // this.azureService = azureService;
        // this.argoCDService = argoCDService;
        // this.azureService= azureService;

    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<UserFront>>> getUserList() {
        var userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'OWNER', 'VIEWER')")
    public ResponseEntity<Mono<UserFront>> getUserByName(@PathVariable String name) {
        var userList = userService.getUserByName(name);
        return ResponseEntity.ok(userList);
    }

    /*
     * @GetMapping("/{keyVaultName}/{secretName}/{secretValue}")
     * public ResponseEntity<Mono<String>> createSecret(@PathVariable String
     * keyVaultName,@PathVariable String secretName,@PathVariable String
     * secretValue) {
     * var userList = this.azureService.createSecretAsync(keyVaultName, secretName,
     * secretValue).block();
     * return ResponseEntity.ok(Mono.just(keyVaultName));
     * }
     */

}
