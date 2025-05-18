package ru.mdm.documents.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.entity.DocumentSign;

import java.util.UUID;

/**
 * Репозиторий для работы с подписанием документов.
 */
@Repository
public interface DocumentSignRepository extends ReactiveSortingRepository<DocumentSign, UUID>, ReactiveCrudRepository<DocumentSign, UUID> {

    Flux<DocumentSign> findByDocumentId(UUID documentId);

    Mono<DocumentSign> findByDocumentIdAndUserLogin(UUID documentId, String userLogin);

    Mono<Void> deleteByDocumentId(UUID documentId);
}
