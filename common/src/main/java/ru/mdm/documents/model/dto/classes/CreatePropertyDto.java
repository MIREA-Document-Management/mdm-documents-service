package ru.mdm.documents.model.dto.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.mdm.documents.model.PropertyType;

@Data
@Schema(description = "Модель для создания атрибута класса документа")
public class CreatePropertyDto {

    @NotBlank(message = "Отсутствует код атрибута")
    @Schema(description = "Код атрибута")
    private String code;

    @NotBlank(message = "Отсутствует наименование атрибута")
    @Schema(description = "Наименование атрибута")
    private String name;

    @Schema(description = "Описание атрибута")
    private String description;

    @NotNull(message = "Отсутствует тип атрибута")
    @Schema(description = "Тип атрибута")
    private PropertyType type;

    @NotNull(message = "Отсутствует признак обязательности")
    @Schema(description = "Признак обязательности")
    private Boolean required;
}
