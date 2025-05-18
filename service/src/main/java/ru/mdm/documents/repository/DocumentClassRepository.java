package ru.mdm.documents.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.entity.DocumentClass;

import java.util.UUID;

/**
 * Репозиторий для работы с классами документов.
 */
@Repository
public interface DocumentClassRepository extends ReactiveSortingRepository<DocumentClass, UUID>, ReactiveCrudRepository<DocumentClass, UUID> {

    Flux<DocumentClass> findAllBy(Pageable pageable);

    Mono<DocumentClass> findByClassName(String className);
}
