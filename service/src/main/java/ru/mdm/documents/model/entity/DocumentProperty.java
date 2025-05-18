package ru.mdm.documents.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.mdm.documents.model.PropertyType;

import java.util.UUID;

/**
 * Сущность атрибута документа.
 */
@Data
@Table("mdm_document_properties")
public class DocumentProperty {

    /**
     * Идентификатор класса.
     */
    @Id
    @Column("id")
    private UUID id;

    /**
     * Идентфикатор класса, к которому привязан атрибут.
     */
    @Column("class_id")
    private UUID classId;

    /**
     * Код атрибута.
     */
    @Column("code")
    private String code;

    /**
     * Наименование атрибута.
     */
    @Column("name")
    private String name;

    /**
     * Описание атрибута.
     */
    @Column("description")
    private String description;

    /**
     * Тип атрибута.
     */
    @Column("type")
    private PropertyType type;

    /**
     * Признак обязательности.
     */
    @Column("required")
    private Boolean required;
}
