package com.nttdata.knot.baseapi.Interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.ProductPackage.Product;

import reactor.core.publisher.Mono;

public interface IProductService {

    Mono<Boolean> createProductAsync(Product product) throws JsonProcessingException;

    Mono<Boolean> deleteProductAsync(String organization, String area, String id) throws JsonProcessingException;

    Mono<Boolean> updateProductAsync(Product product) throws JsonProcessingException;

    Mono<List<Product>> getProductList(String username);

    Mono<Product> getProductByID(String organization, String area, String id);

    Mono<List<Component>> getComponentListOfAProduct(String organization, String area, String id);

    Mono<List<Object>> getComponentListOfAProductByFilter(String organization, String area, String id, String filter);

}