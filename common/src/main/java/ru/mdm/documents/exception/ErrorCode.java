package ru.mdm.documents.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DOCUMENT_NOT_FOUND("Документ с id='%s' не найден"),
    VERSION_NOT_FOUND("Документ с версией '%s' не найден"),
    FORBIDDEN("Нет доступа на выполнение операции"),
    CANNOT_CREATE_CLASS("Не удалось создать класс документа"),
    CLASS_NOT_FOUND("Код класса с id %s не найден"),
    CANNOT_UPDATE_CLASS("Не удалось обновить класс документа"),
    CANNOT_DELETE_CLASS("Не удалось удалить класс документа"),
    CANNOT_GET_CLASS("Не удалось получить класс документа"),
    CANNOT_GET_CLASSES("Не удалось получить список классов документов"),
    CLASS_NAME_NOT_FOUND("Класс документа %s не найден"),
    INCORRECT_TYPE("Атрибут с кодом %s должен быть формата %s")
    ;

    private final String text;

    public String buildErrorText(Object... params) {
        return String.format(this.getText(), params);
    }
}
