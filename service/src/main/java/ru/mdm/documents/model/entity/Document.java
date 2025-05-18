package ru.mdm.documents.model.entity;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.mdm.documents.model.AccessLevel;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность документа.
 */
@Data
@Table("mdm_documents")
public class Document {

    /**
     * Идентификатор документа.
     */
    @Column("id")
    private UUID id;

    /**
     * Идентификатор версии документа.
     */
    @Id
    @Column("version_id")
    private UUID versionId;

    /**
     * Порядковый номер версии документа.
     */
    @Column("version_number")
    private Integer versionNumber;

    /**
     * Наименование документа.
     */
    @Column("name")
    private String name;

    /**
     * Класс документа.
     */
    @Column("class_name")
    private String className;

    /**
     * Описание документа.
     */
    @Column("description")
    private String description;

    /**
     * Номер версии изменения документа.
     */
    @Version
    @Column("record_version")
    private Integer recordVersion;

    /**
     * Уровень доступа.
     */
    @Column("access_level")
    private AccessLevel accessLevel;

    /**
     * Идентифкатор привязанного файла.
     */
    @Column("file_id")
    private UUID fileId;

    /**
     * Кто создал документ.
     */
    @CreatedBy
    @Column("created_by")
    private String createdBy;

    /**
     * Дата и время создания.
     */
    @CreatedDate
    @Column("creation_date")
    private LocalDateTime creationDate;

    /**
     * Кто последний изменил документ.
     */
    @LastModifiedBy
    @Column("modified_by")
    private String modifiedBy;

    /**
     * Дата и время последнего изменения.
     */
    @LastModifiedDate
    @Column("modification_date")
    private LocalDateTime modificationDate;

    /**
     * Дополнительная информация о документе.
     */
    @Column("data")
    private Map<String, Object> data;
}
