package com.nttdata.knot.baseapi.Services;

import com.nttdata.knot.baseapi.Models.BlueprintPackage.Blueprint;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Area;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Interfaces.IAdministrationService;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Organization;

import java.util.*;

import reactor.core.publisher.Mono;

@Service
public class AdministrationService implements IAdministrationService {

        private final Logger logger = LoggerFactory.getLogger(AdministrationService.class);
        public WebClient webClient;

        @Autowired
        public AdministrationService(@Qualifier("apiWebClient") WebClient webClient) {
                this.webClient = webClient;
        }

        @Override
        public Mono<List<Object>> getOrganizationList() throws JsonProcessingException {
                String uri = "/administration-api/organization";

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToFlux(Object.class)
                        .collectList()
                        .doOnNext(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Organization> getOrganizationByName(String id) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + id;

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Organization.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Boolean> createOrganization(Organization organization) throws JsonProcessingException {
                String uri = "/administration-api/organization";

                return webClient.post()
                        .uri(uri)
                        .bodyValue(organization)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Organization> updatedOrganization(Organization organization) throws JsonProcessingException {
                String uri = "/administration-api/organization";

                return webClient.put()
                        .uri(uri)
                        .bodyValue(organization)
                        .retrieve()
                        .bodyToMono(Organization.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Organization> deleteOrganization(String id) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + id;

                return webClient.delete()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Organization.class)
                        .doOnSuccess(response -> { this.logger.info("Request sent"); })
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<List<Area>> getAreaList(String idOrganization) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + idOrganization + "/areas";

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToFlux(Area.class)
                        .collectList()
                        .doOnNext(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Boolean> createArea(Area newArea, String idOrganization) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + idOrganization + "/areas";

                return webClient.post()
                        .uri(uri)
                        .bodyValue(newArea)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Area> updatedArea(Area updatedArea, String idOrganization) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + idOrganization + "/areas";

                return webClient.put()
                        .uri(uri)
                        .bodyValue(updatedArea)
                        .retrieve()
                        .bodyToMono(Area.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Boolean> deleteArea(String idOrganization, String idArea) throws JsonProcessingException {
                String uri = "/administration-api/organization/" + idOrganization + "/areas/" + idArea;

                return webClient.delete()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .doOnSuccess(response -> { this.logger.info("Request sent"); })
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<List<Blueprint>> getBlueprintList(String idOrganization, String idArea)
                        throws JsonProcessingException {
                String uri = "/administration-api/blueprint/" + idOrganization + "/" + idArea;

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToFlux(Blueprint.class)
                        .collectList()
                        .doOnNext(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Boolean> createBlueprint(Blueprint blueprint, String idOrganization, String idArea)
                        throws JsonProcessingException {
                String uri = "/administration-api/blueprint/" + idOrganization + "/" + idArea;

                return webClient.post()
                        .uri(uri)
                        .bodyValue(blueprint)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Blueprint> updatedBlueprint(Blueprint updatedBlueprint, String idOrganization, String idArea)
                        throws JsonProcessingException {
                String uri = "/administration-api/blueprint/" + idOrganization + "/" + idArea;

                return webClient.put()
                        .uri(uri)
                        .bodyValue(updatedBlueprint)
                        .retrieve()
                        .bodyToMono(Blueprint.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Blueprint> deleteBlueprint(String idOrganization, String idArea, String idBlueprint)
                        throws JsonProcessingException {
                String uri = "/administration-api/blueprint/" + idOrganization + "/" + idArea + "/" + idBlueprint;

                return webClient.delete()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Blueprint.class)
                        .doOnSuccess(response -> { this.logger.info("Request sent"); })
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<Object> getConfig() throws JsonProcessingException {
                String uri = "/administration-api/config";

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(Object.class)
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }

        @Override
        public Mono<List<Object>> getCatalog() throws JsonProcessingException {
                String uri = "/administration-api/catalog";

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToFlux(Object.class)
                        .collectList()
                        .doOnSuccess(response -> this.logger.info("Request sent"))
                        .doOnError(e -> this.logger.error("Unable to process request, exception is " + e.getMessage()));
        }
}
