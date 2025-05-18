package ru.mdm.documents.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.model.dto.classes.CreateDocumentClassDto;
import ru.mdm.documents.model.dto.classes.DocumentClassDto;
import ru.mdm.documents.model.dto.classes.UpdateDocumentClassDto;

import java.util.UUID;

/**
 * REST-API для работы с классами документами.
 */
public interface DocumentClassRestApi {

    String BASE_URI = "/api/v1/classes";

    @Operation(summary = "Создать класс документа",
            description = "Создать новый класс документа",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Класс успешно создан",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentClassDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<DocumentClassDto> createClass(@RequestBody CreateDocumentClassDto dto);

    @Operation(summary = "Обновить класс документа",
            description = "Обновить класс документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Класс успешно обновлен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentClassDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Класс не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PutMapping("/{id}")
    Mono<DocumentClassDto> updateClass(@PathVariable UUID id, @RequestBody UpdateDocumentClassDto dto);

    @Operation(summary = "Удалить класс документа",
            description = "Удалить класс документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Класс успешно удален",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentClassDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Класс не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @DeleteMapping("/{id}")
    Mono<DocumentClassDto> deleteClass(@PathVariable UUID id);

    @Operation(summary = "Получить класс документа",
            description = "Получить класс документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentClassDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Класс не найден",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{id}")
    Mono<DocumentClassDto> getDocumentClass(@PathVariable UUID id);

    @Operation(summary = "Получить список классов документа",
            description = "Получить список классов документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DocumentClassDto.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный формат переданных значений",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping
    Flux<DocumentClassDto> getClasses(Pageable pageable);
}
