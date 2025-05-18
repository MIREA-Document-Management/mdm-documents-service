package ru.mdm.documents.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.entity.DocumentProperty;

import java.util.UUID;

/**
 * Репозиторий для работы с атрибутами документов.
 */
@Repository
public interface DocumentPropertyRepository extends ReactiveSortingRepository<DocumentProperty, UUID>, ReactiveCrudRepository<DocumentProperty, UUID> {

    Mono<Void> deleteByClassId(UUID classId);

    Flux<DocumentProperty> findByClassId(UUID classId);
}
