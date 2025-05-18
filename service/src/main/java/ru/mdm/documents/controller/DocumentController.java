package ru.mdm.documents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.api.DocumentRestApi;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;
import ru.mdm.documents.service.DocumentService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(DocumentRestApi.BASE_URI)
public class DocumentController implements DocumentRestApi {

    private final DocumentService documentServiceImpl;

    @Override
    public Mono<DocumentDto> createDocument(CreateDocumentDto dto, ServerWebExchange exchange) {
        return documentServiceImpl.createDocument(dto);
    }

    @Override
    public Mono<DocumentDto> createDocumentRaw(String dto, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<DocumentDto> createDocumentVersion(UUID id, CreateDocumentDto dto) {
        return documentServiceImpl.createDocumentVersion(id, dto);
    }

    @Override
    public Mono<DocumentDto> updateDocument(UUID id, UUID versionId, UpdateDocumentDto dto) {
        return documentServiceImpl.updateDocument(id, versionId, dto);
    }

    @Override
    public Mono<DocumentDto> getDocument(UUID id, UUID versionId) {
        return documentServiceImpl.getDocument(id, versionId);
    }

    @Override
    public Flux<DocumentDto> getDocuments(Pageable pageable) {
        return documentServiceImpl.getDocuments(pageable);
    }

    @Override
    public Flux<DocumentDto> getDocumentVersions(UUID id, Pageable pageable) {
        return documentServiceImpl.getDocumentVersions(id, pageable);
    }

    @Override
    public Mono<DocumentDto> deleteDocumentVersion(UUID id, UUID versionId) {
        return documentServiceImpl.deleteDocumentVersion(id, versionId);
    }

    @Override
    public Mono<Void> deleteDocumentWithAllVersions(UUID id) {
        return documentServiceImpl.deleteDocumentWithAllVersions(id);
    }
}
