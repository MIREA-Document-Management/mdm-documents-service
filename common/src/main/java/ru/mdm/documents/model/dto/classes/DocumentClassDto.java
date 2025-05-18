package ru.mdm.documents.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Модель класса документа")
public class DocumentClassDto {

    @Schema(description = "Идентификатор класса")
    private UUID id;

    @Schema(description = "Наименование класса документа")
    private String className;

    @Schema(description = "Описание класса документа")
    private String description;

    @Schema(description = "Атрибуты класса")
    private List<PropertyDto> properties;

    @Schema(description = "Кто создал")
    private String createdBy;

    @Schema(description = "Дата и время создания")
    private LocalDateTime creationDate;

    @Schema(description = "Кто последний изменил")
    private String modifiedBy;

    @Schema(description = "Дата и время последнего изменения")
    private LocalDateTime modificationDate;
}
