package ru.mdm.documents.model.dto.documents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSigner {

    @Schema(description = "Логин подписанта")
    private String login;

    @Schema(description = "Подписан ли документ пользователем")
    private Boolean signed;
}
