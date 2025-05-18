package ru.mdm.documents.client.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import ru.mdm.documents.client.feign.DocumentsServiceFeignClient;

/**
 * Автоконфигурация клиента сервиса.
 */
@Configuration
@ComponentScan(basePackages = "ru.mdm.documents.client")
@EnableReactiveFeignClients(clients = {DocumentsServiceFeignClient.class})
public class DocumentsClientAutoConfiguration {

    @Value("${mdm.documents.service.url}")
    private String serviceUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.create(serviceUrl);
    }
}
