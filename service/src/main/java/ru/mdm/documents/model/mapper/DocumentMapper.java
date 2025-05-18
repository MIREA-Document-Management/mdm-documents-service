package ru.mdm.documents.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;
import ru.mdm.documents.model.entity.Document;

@Mapper
public interface DocumentMapper {

    Document toEntity(CreateDocumentDto dto);

    DocumentDto toDto(Document entity);

    Document update(@MappingTarget Document entity, UpdateDocumentDto dto);
}
