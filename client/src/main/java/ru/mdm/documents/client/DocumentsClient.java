package ru.mdm.documents.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.client.feign.DocumentsServiceFeignClient;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;
import ru.mdm.documents.service.DocumentService;

import java.util.UUID;

/**
 * Реализация клиента для работы с документами.
 */
@Service
@RequiredArgsConstructor
public class DocumentsClient implements DocumentService {

    private final DocumentsServiceFeignClient documentsServiceFeignClient;

    @Override
    public Mono<DocumentDto> createDocument(CreateDocumentDto dto) {
        return documentsServiceFeignClient.createDocument(dto);
    }

    @Override
    public Mono<DocumentDto> createDocumentVersion(UUID id, CreateDocumentDto dto) {
        return documentsServiceFeignClient.createDocumentVersion(id, dto);
    }

    @Override
    public Mono<DocumentDto> updateDocument(UUID id, UUID versionId, UpdateDocumentDto dto) {
        return documentsServiceFeignClient.updateDocument(id, versionId, dto);
    }

    @Override
    public Mono<DocumentDto> getDocument(UUID id, UUID versionId) {
        return documentsServiceFeignClient.getDocument(id, versionId);
    }

    @Override
    public Flux<DocumentDto> getDocuments(Pageable pageable) {
        return documentsServiceFeignClient.getDocuments(pageable);
    }

    @Override
    public Flux<DocumentDto> getDocumentVersions(UUID id, Pageable pageable) {
        return documentsServiceFeignClient.getDocumentVersions(id, pageable);
    }

    @Override
    public Mono<DocumentDto> deleteDocumentVersion(UUID id, UUID versionId) {
        return documentsServiceFeignClient.deleteDocumentVersion(id, versionId);
    }

    @Override
    public Mono<Void> deleteDocumentWithAllVersions(UUID id) {
        return documentsServiceFeignClient.deleteDocumentWithAllVersions(id);
    }
}
