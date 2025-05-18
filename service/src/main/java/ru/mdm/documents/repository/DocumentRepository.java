package ru.mdm.documents.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.entity.Document;

import java.util.UUID;

/**
 * Репозиторий для работы с документами.
 */
@Repository
public interface DocumentRepository extends ReactiveSortingRepository<Document, UUID>, ReactiveCrudRepository<Document, UUID> {

    Mono<Document> findByIdAndVersionId(UUID id, UUID versionId);

    @Query("""
            SELECT * FROM mdm_documents
            WHERE id = version_id
            """)
    Flux<Document> findAllHeadDocuments(Pageable pageable);

    @Query("""
            SELECT * FROM mdm_documents
            WHERE id = :id
            """)
    Flux<Document> findAllDocumentVersions(UUID id, Pageable pageable);

    @Query("""
            DELETE FROM mdm_documents
            WHERE id = :id
            """)
    Mono<Void> deleteAllDocumentVersions(UUID id);
}
