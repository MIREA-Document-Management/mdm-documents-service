package ru.mdm.documents.model.entity;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность класса документа.
 */
@Data
@Table("mdm_document_classes")
public class DocumentClass {

    /**
     * Идентификатор класса.
     */
    @Id
    @Column("id")
    private UUID id;

    /**
     * Наименование класса документа.
     */
    @Column("className")
    private String className;

    /**
     * Описание класса документа.
     */
    @Column("description")
    private String description;

    /**
     * Кто создал.
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
     * Кто последний изменил.
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
}
