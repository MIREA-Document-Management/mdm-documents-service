package ru.mdm.documents.model.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность конкретного подписания документа.
 */
@Data
@Table("mdm_document_user_signs")
public class DocumentSign {

    /**
     * Идентификатор подписания.
     */
    @Column("id")
    private UUID id;

    /**
     * Идентификатор документа.
     */
    @Column("document_id")
    private UUID documentId;

    /**
     * Логин пользователя, который подписал документ.
     */
    @Column("user_login")
    private String userLogin;

    /**
     * Признак подписания пользователем документа.
     */
    @Column("signed")
    private Boolean signed;

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
