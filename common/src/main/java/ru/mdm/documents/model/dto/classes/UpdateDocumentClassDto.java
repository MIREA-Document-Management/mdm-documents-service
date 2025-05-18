package ru.mdm.documents.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Schema(description = "Модель для обновления класса документа")
public class UpdateDocumentClassDto {

    @Schema(description = "Описание класса документа")
    private String description;

    @Schema(description = "Атрибуты класса")
    private List<@Valid CreatePropertyDto> properties;
}
