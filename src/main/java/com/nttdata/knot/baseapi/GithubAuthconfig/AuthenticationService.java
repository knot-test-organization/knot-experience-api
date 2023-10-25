package com.nttdata.knot.baseapi.GithubAuthconfig;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.knot.baseapi.GithubAuthconfig.models.Edge;
import com.nttdata.knot.baseapi.GithubAuthconfig.models.GithubTeams;
import com.nttdata.knot.baseapi.GithubAuthconfig.models.Node;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final String githubGraphqlUri;
    private final String githubUserUri;
    private final String githubOrganization;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(@Value("${auth.github.user-uri}") String githubUserUri,
            @Value("${auth.github.graphql-uri}") String githubGraphqlUri,
            @Value("${github.organization}") String githubOrganization) {
        this.githubGraphqlUri = githubGraphqlUri;
        this.githubUserUri = githubUserUri;
        this.githubOrganization = githubOrganization;
    }

    @Override
    public GithubUserInfo introspect(String token) {
        try {
            // callingGraph(token);
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            WebClient webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
            return getGithubUserInfo(webClient, token);
        } catch (SSLException | JsonProcessingException e) {
            throw new OAuth2IntrospectionException("Unauthorized");
        }
    }

    public List<String> getGithubUserTeams(WebClient webClient, String token) {

        // query for teams where token holder is maintainer
        String adminQuery = "{\"query\":\"{ organization(login: \\\"" + this.githubOrganization
                + "\\\") { teams(first: 10, role: ADMIN) { edges { node { name }}}}}\",\"variables\":{}}";

        // query for teams where token holder is member
        String memberQuery = "{\"query\":\"{ organization(login: \\\"" + this.githubOrganization
                + "\\\") { teams(first: 10, role: MEMBER) { edges { node { name }}}}}\",\"variables\":{}}";

        // run the two queries
        List<String> adminTeams = getTeams(webClient, token, adminQuery);
        List<String> memberTeams = getTeams(webClient, token, memberQuery);

        // Combine the results for list of all teams in one list
        List<String> allTeams = new ArrayList<>();
        allTeams.addAll(adminTeams);
        allTeams.addAll(memberTeams);

        return allTeams;
    }

    // method to query github teams a token holder is part of
    private List<String> getTeams(WebClient webClient, String token, String query) {
        List<String> list = new ArrayList<>();
        GithubTeams responseGraph = webClient
                .post()
                .uri(this.githubGraphqlUri)
                .headers(httpHeaders -> {
                    httpHeaders.setBearerAuth(token);
                })
                .bodyValue(query).retrieve().bodyToMono(GithubTeams.class).block();

        if (responseGraph.data != null)

            list = responseGraph.data.organization.teams.edges.stream()
                    .map(Edge::getNode).toList().stream().map(Node::getName).toList();

        return list;
    }

    public GithubUserInfo getGithubUserInfo(WebClient webClient, String token) throws JsonProcessingException {

        ResponseEntity<JsonNode> jsonResponse = webClient
                .get()
                .uri(this.githubUserUri)
                .headers(httpHeaders -> {
                    httpHeaders.setBearerAuth(token);
                })

                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    throw new OAuth2IntrospectionException("Unauthorized");
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    throw new OAuth2IntrospectionException("Unauthorized");
                })
                // .bodyToMono(String.class)
                .toEntity(JsonNode.class)
                .block();

        assert jsonResponse != null;
        assert jsonResponse.getBody() != null;

        // set the githubUserInfo class according to the API response
        GithubUserInfo githubUserInfo = new ObjectMapper().readValue(jsonResponse.getBody().toString(),
                GithubUserInfo.class);

        githubUserInfo.setScope(jsonResponse.getHeaders().getValuesAsList("X-OAuth-Scopes"));

        /**
         * VERIFY USER’S AUTHENTICATION AND ASSIGN ROLES BASED ON TEAM MEMBERSHIP IN
         * SPECIFIED ORGANIZATION
         **/

        // create Github user list of autorities
        List<String> authorities = new ArrayList<>(List.copyOf(getGithubUserTeams(webClient, token)));

        // Check if the user is an active member of any team in the organization.
        if (!authorities.isEmpty()) {

            // Determine the user's role accordingly
            if (authorities.stream().anyMatch(name -> name.toLowerCase().contains("knot-platform-admins"))) {

                // This role allows the creation of organisations, blueprints, etc.
                authorities.add("ADMIN");

            } else if (authorities.stream().anyMatch(name -> name.toLowerCase().contains("knot-security-admins"))) {

                // This role allows the assignment of users to Knot cross-cutting groups
                authorities.add("OPERATOR"); // other name exemple : IT

            } else if (authorities.stream().anyMatch(name -> name.toLowerCase().contains("knot-product-owners"))) {

                // This role allows the creation of products and components
                authorities.add("OWNER");

            } else if (authorities.stream().anyMatch(name -> name.toLowerCase().contains("knot-product-viewers"))) {

                // This role allows to view the products and components in which user is  involved
                authorities.add("VIEWER"); // Other name exemples : inspector , monitor, devoloper
            }

        } else {

            logger.info("The user is not assigned to any team in {} organization", this.githubOrganization);
        }

        githubUserInfo.setAuthorities(authorities);

        return githubUserInfo;
    }
}
