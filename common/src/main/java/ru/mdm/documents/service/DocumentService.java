package ru.mdm.documents.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;

import java.util.UUID;

/**
 * Сервис для работы с документами.
 */
public interface DocumentService {

    /**
     * Создать документ.
     *
     * @param dto модель для создания документа
     * @return созданный документ
     */
    Mono<DocumentDto> createDocument(@Valid CreateDocumentDto dto);

    /**
     * Создать новую версию документа.
     *
     * @param id идентификатор документа
     * @param dto модель для создания новой версии
     * @return новая версия документа
     */
    Mono<DocumentDto> createDocumentVersion(@NotNull UUID id, @Valid CreateDocumentDto dto);

    /**
     * Обновить конкретную версию документа.
     *
     * @param id идентификатор документа
     * @param versionId идентфикатор версии документа
     * @param dto модель для обновления версии
     * @return обновленная версия документа
     */
    Mono<DocumentDto> updateDocument(@NotNull UUID id, @NotNull UUID versionId, @Valid UpdateDocumentDto dto);

    /**
     * Получить конкретную версию документа.
     *
     * @param id идентификатор документа
     * @param versionId идентфикатор версии документа
     * @return версия документа
     */
    Mono<DocumentDto> getDocument(@NotNull UUID id, @NotNull UUID versionId);

    /**
     * Получить список документов.
     *
     * @param pageable параметры страницы
     * @return список документов
     */
    Flux<DocumentDto> getDocuments(Pageable pageable);

    /**
     * Получить список версий документа.
     *
     * @param id идентификатор документа
     * @param pageable параметры страницы
     * @return список версий
     */
    Flux<DocumentDto> getDocumentVersions(@NotNull UUID id, Pageable pageable);

    /**
     * Удалить версию документа.
     *
     * @param id идентификатор документа
     * @param versionId идентфикатор версии документа
     * @return удаленный документ
     */
    Mono<DocumentDto> deleteDocumentVersion(@NotNull UUID id, @NotNull UUID versionId);

    /**
     * Удалить документ со всеми его версиями.
     *
     * @param id идентификатор документа
     */
    Mono<Void> deleteDocumentWithAllVersions(@NotNull UUID id);
}
