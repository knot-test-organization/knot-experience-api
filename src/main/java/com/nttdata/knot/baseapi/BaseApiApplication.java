package com.nttdata.knot.baseapi;

import javax.net.ssl.SSLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

@SpringBootApplication
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://knot.westeurope.cloudapp.azure.com/experience-api", description = "Production Knot Experience API server")
        },
        info = @Info(title = "Knot Experience API")
)

public class BaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }

    @Bean
    public HttpClient httpClient() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        return HttpClient.create().secure(t -> t.sslContext(sslContext));
    }

    //     @Bean
    // public WebMvcConfigurer corsConfigurer2() {
    //    return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                      .allowedOrigins("*")
    //                      .allowedMethods("*")
    //                      .allowedHeaders("*");
    //          }
    //     };
    // }
}
