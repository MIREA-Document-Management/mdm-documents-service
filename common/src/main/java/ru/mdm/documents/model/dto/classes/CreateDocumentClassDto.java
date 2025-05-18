package ru.mdm.documents.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Модель для создания класса документа")
public class CreateDocumentClassDto {

    @NotBlank(message = "Отсутствует наименование класса документа")
    @Schema(description = "Наименование класса документа")
    private String className;

    @Schema(description = "Описание класса документа")
    private String description;

    @Schema(description = "Атрибуты класса")
    private List<@Valid CreatePropertyDto> properties;
}
