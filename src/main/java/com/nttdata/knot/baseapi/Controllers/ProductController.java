package com.nttdata.knot.baseapi.Controllers;

import com.azure.core.annotation.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Interfaces.IProductService;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.ArgoCdApplication;
// import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project.ArgoCdProject;
import com.nttdata.knot.baseapi.Models.ProductPackage.Product;
// import com.nttdata.knot.baseapi.Services.ArgoCDService;
//import com.nttdata.knot.baseapi.Services.AzureService;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/listProducts/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<Product>>> getProductList(@PathVariable String username, Authentication auth) {
        // var authorities = auth.getAuthorities();
        Mono<List<Product>> productList = this.productService.getProductList(username);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{organization}/{area}/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<Product>> getProductByID(@PathVariable String organization, @PathVariable String area, @PathVariable String id, Authentication auth) {
        // boolean hasAuthority = true;
        Mono<Product> product = null;

        // for (GrantedAuthority authority : auth.getAuthorities()) {
        //     if (authority.getAuthority().contains("knot-product-" + organization + "-" + area + "-" + id + "-leads") ||
        //             authority.getAuthority().contains("knot-product-" + organization + "-" + area + "-" + id + "-developers")) {
        //         hasAuthority = true;
        //         break;
        //     };
        // };

        // if (hasAuthority) {
            product = this.productService.getProductByID(organization, area, id);
        // };

        return ResponseEntity.ok(product);
    }

    @GetMapping("/{organization}/{area}/{id}/components")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<Component>>> getComponentListOfAProduct(@PathVariable String organization, @PathVariable String area, @PathVariable String id, Authentication auth) {
        Mono<List<Component>> componentList = this.productService.getComponentListOfAProduct(organization, area, id);
        return ResponseEntity.ok(componentList);
    }

    @GetMapping("/{organization}/{area}/{id}/components/{filter}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER', 'VIEWER')")
    public ResponseEntity<Mono<List<Object>>> getComponentListOfAProductByFilter(@PathVariable String organization, @PathVariable String area, @PathVariable String id, @PathVariable String filter, Authentication auth) {
        Mono<List<Object>> componentList = this.productService.getComponentListOfAProductByFilter(organization, area, id, filter);
        return ResponseEntity.ok(componentList);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Boolean>> createProduct(@RequestBody Product product) throws JsonProcessingException {
        Mono<Boolean> canAddProduct = this.productService.createProductAsync(product);
        return ResponseEntity.ok(canAddProduct);
    }
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Boolean>> updateProduct(@RequestBody Product product) throws JsonProcessingException {
        Mono<Boolean> canUpdateProduct = this.productService.updateProductAsync(product);
        return ResponseEntity.ok(canUpdateProduct);
    }
    @DeleteMapping("/{org}/{area}/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR','OWNER')")
    public ResponseEntity<Mono<Boolean>> deleteProduct(@PathVariable String org, @PathVariable String area, @PathVariable String id) throws JsonProcessingException {
        Mono<Boolean> canDeleteProduct = this.productService.deleteProductAsync( org, area, id);
        return ResponseEntity.ok(canDeleteProduct);
    }
}
