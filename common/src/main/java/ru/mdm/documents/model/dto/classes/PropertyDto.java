package ru.mdm.documents.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.mdm.documents.model.PropertyType;

@Data
@Schema(description = "Модель атрибута класса документа")
public class PropertyDto {

    @Schema(description = "Код атрибута")
    private String code;

    @Schema(description = "Наименование атрибута")
    private String name;

    @Schema(description = "Описание атрибута")
    private String description;

    @Schema(description = "Тип атрибута")
    private PropertyType type;

    @Schema(description = "Признак обязательности")
    private Boolean required;
}
