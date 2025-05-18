package ru.mdm.documents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.api.DocumentClassRestApi;
import ru.mdm.documents.model.dto.classes.CreateDocumentClassDto;
import ru.mdm.documents.model.dto.classes.DocumentClassDto;
import ru.mdm.documents.model.dto.classes.UpdateDocumentClassDto;
import ru.mdm.documents.service.DocumentClassService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(DocumentClassRestApi.BASE_URI)
public class DocumentClassController implements DocumentClassRestApi {

    private final DocumentClassService documentClassServiceImpl;

    @Override
    public Mono<DocumentClassDto> createClass(CreateDocumentClassDto dto) {
        return documentClassServiceImpl.createClass(dto);
    }

    @Override
    public Mono<DocumentClassDto> updateClass(UUID id, UpdateDocumentClassDto dto) {
        return documentClassServiceImpl.updateClass(id, dto);
    }

    @Override
    public Mono<DocumentClassDto> deleteClass(UUID id) {
        return documentClassServiceImpl.deleteClass(id);
    }

    @Override
    public Mono<DocumentClassDto> getDocumentClass(UUID id) {
        return documentClassServiceImpl.getDocumentClass(id);
    }

    @Override
    public Flux<DocumentClassDto> getClasses(Pageable pageable) {
        return documentClassServiceImpl.getClasses(pageable);
    }
}
