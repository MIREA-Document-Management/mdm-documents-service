package ru.mdm.documents.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;

import java.util.UUID;

/**
 * REST-API для работы с документами.
 */
public interface DocumentRestApi {

    String BASE_URI = "/api/v1/documents";

    @Operation(summary = "Создать документ",
            description = "Создать новый документ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Документ успешно создан",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<DocumentDto> createDocument(@RequestBody CreateDocumentDto dto, ServerWebExchange exchange);

    @Operation(summary = "Создать документ",
            description = "Создать новый документ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Документ успешно создан",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/raw")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<DocumentDto> createDocumentRaw(@RequestBody String dto, ServerWebExchange exchange);

    @Operation(summary = "Создать новую версию документа",
            description = "Создать новую версию документа",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Версия успешно создана",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<DocumentDto> createDocumentVersion(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id,
            @RequestBody CreateDocumentDto dto
    );

    @Operation(summary = "Обновить документ",
            description = "Обновить параметры конкретной версии документа по идентификатору документа и идентификатору версии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PutMapping("/{id}/versions/{versionId}")
    Mono<DocumentDto> updateDocument(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Идентфикатор версии документа", required = true)
            @PathVariable UUID versionId,
            @RequestBody UpdateDocumentDto dto
    );

    @Operation(summary = "Получить документ",
            description = "Получить конкретную версию документа по его идентификатору и идентификатору версии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{id}/versions/{versionId}")
    Mono<DocumentDto> getDocument(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Идентфикатор версии документа", required = true)
            @PathVariable UUID versionId
    );

    @Operation(summary = "Получить список документов",
            description = "Получить список документов с заданными параметрами страницы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping
    Flux<DocumentDto> getDocuments(Pageable pageable);

    @Operation(summary = "Получить список версий документа",
            description = "Получить список версий документа по идентификатору документа с заданными параметрами страницы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{id}")
    Flux<DocumentDto> getDocumentVersions(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id,
            Pageable pageable
    );

    @Operation(summary = "Удалить версию документа",
            description = "Удалить конкретную версию документа по его идентификатору и идентификатору версии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @DeleteMapping("/{id}/versions/{versionId}")
    Mono<DocumentDto> deleteDocumentVersion(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Идентфикатор версии документа", required = true)
            @PathVariable UUID versionId
    );

    @Operation(summary = "Удалить все версии документа",
            description = "Удалить документ вместе со всеми его версиями",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Операция запрещена",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @DeleteMapping("/{id}")
    Mono<Void> deleteDocumentWithAllVersions(
            @Parameter(description = "Идентфикатор документа", required = true)
            @PathVariable UUID id
    );
}
