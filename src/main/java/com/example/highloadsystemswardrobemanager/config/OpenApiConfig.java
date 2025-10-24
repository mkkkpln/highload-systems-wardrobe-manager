package com.example.highloadsystemswardrobemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация OpenAPI/Swagger для автоматической генерации документации API.
 * Swagger UI доступен по адресу: /swagger-ui.html
 * OpenAPI спецификация в JSON: /v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wardrobeManagerOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Локальный сервер разработки");

        Server dockerServer = new Server();
        dockerServer.setUrl("http://localhost:8080");
        dockerServer.setDescription("Docker окружение");

        Contact contact = new Contact();
        contact.setName("Wardrobe Manager API Support");
        contact.setEmail("support@wardrobemanager.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Wardrobe Manager API")
                .version("1.0")
                .contact(contact)
                .description("REST API для управления гардеробом и образами пользователей. " +
                        "Позволяет создавать, изменять и удалять предметы одежды, " +
                        "формировать образы из них, а также управлять пользователями системы.")
                .termsOfService("https://example.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, dockerServer));
    }
}

