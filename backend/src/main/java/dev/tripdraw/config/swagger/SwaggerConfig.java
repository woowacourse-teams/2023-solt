package dev.tripdraw.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(url())
                .components(authorization())
                .info(info());
    }

    private Server url() {
        return new Server()
                .url("/");
    }

    private Components authorization() {
        return new Components()
                .addHeaders(HttpHeaders.AUTHORIZATION, authorzationHeader());
    }

    private Header authorzationHeader() {
        return new Header()
                .description("Authorization : BASE64(nickname)")
                .schema(new StringSchema());
    }

    private Info info() {
        return new Info()
                .title("트립드로우 서버 API 명세서")
                .version("v0.0.1")
                .description("API Description");
    }
}
