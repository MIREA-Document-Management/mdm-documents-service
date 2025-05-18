package ru.mdm.documents.client.feign;

import reactivefeign.spring.config.ReactiveFeignClient;
import ru.mdm.documents.api.DocumentRestApi;
import ru.mdm.documents.client.configuration.feign.FeignClientConfiguration;

/**
 * Клиент для работы с документами.
 */
@ReactiveFeignClient(name = "mdm-documents-client", configuration = FeignClientConfiguration.class,
        url = "${mdm.documents.service.url}", path = DocumentRestApi.BASE_URI)
public interface DocumentsServiceFeignClient extends DocumentRestApi {
}
