package ru.mdm.documents.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mdm.documents.model.dto.classes.CreatePropertyDto;
import ru.mdm.documents.model.dto.classes.PropertyDto;
import ru.mdm.documents.model.entity.DocumentProperty;

import java.util.UUID;

@Mapper
public interface DocumentPropertyMapper {

    @Mapping(source = "classId", target = "classId")
    DocumentProperty toEntity(CreatePropertyDto dto, UUID classId);

    PropertyDto toDto(DocumentProperty entity);
}
