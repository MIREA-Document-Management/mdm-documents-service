package ru.mdm.documents.model.dto.documents;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.mdm.documents.model.AccessLevel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Schema(description = "Модель для создания документа")
public class CreateDocumentDto {

    @Schema(description = "Код класса документа")
    @NotBlank(message = "Отсутствует класс документа")
    private String className;

    @Schema(description = "Наименование документа")
    @NotBlank(message = "Отсутствует наименование документа")
    private String name;

    @Schema(description = "Описание документа")
    private String description;

    @Schema(description = "Уровень доступа. При создании может быть только PUBLIC или PRIVATE", defaultValue = "PRIVATE")
    private AccessLevel accessLevel = AccessLevel.PRIVATE;

    @Schema(description = "Идентифкатор привязанного файла")
    private UUID fileId;

    @Schema(description = "Логины пользователей, которым нужно подписать документ")
    private List<String> usersToSign;

    @Schema(description = "Дополнительная информация о документе")
    private Map<String, Object> data;

    @JsonAnySetter
    public void addData(String key, Object value) {
        data.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }
}
