package ru.mdm.documents.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.dto.classes.CreateDocumentClassDto;
import ru.mdm.documents.model.dto.classes.DocumentClassDto;
import ru.mdm.documents.model.dto.classes.UpdateDocumentClassDto;

import java.util.UUID;

/**
 * Интерфейс сервиса для работы с классами документов.
 */
public interface DocumentClassService {

    /**
     * Создать класс документа.
     *
     * @param dto модель для создания класса
     * @return созданный класс документа
     */
    Mono<DocumentClassDto> createClass(@Valid CreateDocumentClassDto dto);

    /**
     * Обновить класс документа.
     *
     * @param id идентификатор класса
     * @param dto модель для обновления класса
     * @return обновленный класс документа
     */
    Mono<DocumentClassDto> updateClass(@NotNull UUID id, @Valid UpdateDocumentClassDto dto);

    /**
     * Удалить класс документа.
     *
     * @param id идентификатор класса
     * @return удаленный класс документа
     */
    Mono<DocumentClassDto> deleteClass(@NotNull UUID id);

    /**
     * Получить класс документа.
     *
     * @param id идентификатор класса
     * @return найденный класс документа
     */
    Mono<DocumentClassDto> getDocumentClass(@NotNull UUID id);

    /**
     * Получить список классов документа.
     *
     * @param pageable параметры страницы
     * @return список классов документов
     */
    Flux<DocumentClassDto> getClasses(Pageable pageable);
}
