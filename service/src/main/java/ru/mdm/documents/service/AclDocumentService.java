package ru.mdm.documents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.mdm.acl.service.AclObjectService;
import ru.mdm.documents.model.entity.Document;
import ru.mdm.documents.repository.DocumentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AclDocumentService implements AclObjectService {

    private final DocumentRepository documentRepository;

    @Override
    public Mono<String> getOwnerLogin(UUID objectId) {
        return documentRepository.findById(objectId)
                .map(Document::getCreatedBy);
    }
}
