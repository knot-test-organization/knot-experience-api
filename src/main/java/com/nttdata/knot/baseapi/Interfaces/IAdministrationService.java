package com.nttdata.knot.baseapi.Interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Area;
import com.nttdata.knot.baseapi.Models.OrganizationPackage.Organization;
import com.nttdata.knot.baseapi.Models.BlueprintPackage.Blueprint;

import reactor.core.publisher.Mono;

public interface IAdministrationService {

    Mono<List<Object>> getOrganizationList() throws JsonProcessingException;

    Mono<Organization> getOrganizationByName(String id) throws JsonProcessingException;

    Mono<Boolean> createOrganization(Organization organization) throws JsonProcessingException;

    Mono<Organization> updatedOrganization(Organization organization) throws JsonProcessingException;

    Mono<Organization> deleteOrganization(String id) throws JsonProcessingException;

    Mono<List<Area>> getAreaList(String idOrganization) throws JsonProcessingException;

    Mono<Boolean> createArea(Area area, String idOrganization) throws JsonProcessingException;

    Mono<Area> updatedArea(Area area, String idOrganization) throws JsonProcessingException;

    Mono<Boolean> deleteArea(String idOrganization, String idArea) throws JsonProcessingException;

    Mono<List<Blueprint>> getBlueprintList(String idOrganization, String idArea) throws JsonProcessingException;
    
    Mono<Boolean> createBlueprint(Blueprint blueprint, String idOrganization, String idArea) throws JsonProcessingException;

    Mono<Blueprint> updatedBlueprint(Blueprint updatedBlueprint, String idOrganization, String idArea) throws JsonProcessingException;

    Mono<Blueprint> deleteBlueprint(String idOrganization, String idArea, String idBlueprint) throws JsonProcessingException;

    Mono<Object> getConfig() throws JsonProcessingException;

    Mono<List<Object>> getCatalog() throws JsonProcessingException;

}