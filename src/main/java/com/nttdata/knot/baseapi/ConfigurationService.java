package com.nttdata.knot.baseapi;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class ConfigurationService {

    // argoCD variables
    @Value("${argocd.url}")
    private String argoCDUrl = "";

    @Value("${argocd.token}")
    private String argoCDToken = "";

    // Github variables
    @Value("${github.token}")
    private String githubToken = "";

    private HttpClient httpClient;

    @Autowired
    public ConfigurationService(HttpClient httpClient) throws SSLException {

        this.httpClient = httpClient;
    }
    
    // argoCD WebClient
    @Bean
    public WebClient argoCDWebClient() throws SSLException {

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(this.httpClient))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + argoCDToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(argoCDUrl)
                .build();
    }

    // Github WebClient
    @Bean
    public WebClient githubWebClient() throws SSLException {

        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "HttpRequestsSample")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + this.githubToken)
                .build();
    }

    // vertical APIs WebClient
    @Bean
    public WebClient apiWebClient() throws SSLException {

        return WebClient.builder()
            .baseUrl("http://knot.westeurope.cloudapp.azure.com/")
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

}

