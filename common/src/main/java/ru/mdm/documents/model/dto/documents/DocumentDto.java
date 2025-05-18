package ru.mdm.documents.model.dto.documents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.mdm.documents.model.AccessLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Schema(description = "Модель документа")
public class DocumentDto {

    @Schema(description = "Идентификатор документа")
    private UUID id;

    @Schema(description = "Идентификатор версии документа")
    private UUID versionId;

    @Schema(description = "Порядковый номер версии документа")
    private Integer versionNumber;

    @Schema(description = "Код класса документа")
    private String className;

    @Schema(description = "Наименование документа")
    private String name;

    @Schema(description = "Описание документа")
    private String description;

    @Schema(description = "Номер версии изменения документа")
    private Long recordVersion;

    @Schema(description = "Уровень доступа")
    private AccessLevel accessLevel;

    @Schema(description = "Идентифкатор привязанного файла")
    private UUID fileId;

    @Schema(description = "Пользователи, которым необходимо подписать документ")
    private List<DocumentSigner> signers;

    @Schema(description = "Кто создал документ")
    private String createdBy;

    @Schema(description = "Дата и время создания")
    private LocalDateTime creationDate;

    @Schema(description = "Кто последний изменил документ")
    private String modifiedBy;

    @Schema(description = "Дата и время последнего изменения")
    private LocalDateTime modificationDate;

    @Schema(description = "Дополнительная информация о документе")
    private Map<String, Object> data;
}
