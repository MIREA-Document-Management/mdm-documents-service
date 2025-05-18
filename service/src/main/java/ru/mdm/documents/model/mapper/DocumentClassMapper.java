package ru.mdm.documents.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.mdm.documents.model.dto.classes.CreateDocumentClassDto;
import ru.mdm.documents.model.dto.classes.DocumentClassDto;
import ru.mdm.documents.model.dto.classes.UpdateDocumentClassDto;
import ru.mdm.documents.model.entity.DocumentClass;

@Mapper
public interface DocumentClassMapper {

    DocumentClass toEntity(CreateDocumentClassDto dto);

    DocumentClassDto toDto(DocumentClass entity);

    DocumentClass update(@MappingTarget DocumentClass entity, UpdateDocumentClassDto dto);
}
