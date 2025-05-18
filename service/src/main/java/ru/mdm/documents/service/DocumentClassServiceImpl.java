package ru.mdm.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mdm.documents.exception.ErrorCode;
import ru.mdm.documents.exception.ForbiddenException;
import ru.mdm.documents.exception.ResourceNotFoundException;
import ru.mdm.documents.model.dto.classes.CreateDocumentClassDto;
import ru.mdm.documents.model.dto.classes.CreatePropertyDto;
import ru.mdm.documents.model.dto.classes.DocumentClassDto;
import ru.mdm.documents.model.dto.classes.UpdateDocumentClassDto;
import ru.mdm.documents.model.mapper.DocumentClassMapper;
import ru.mdm.documents.model.mapper.DocumentPropertyMapper;
import ru.mdm.documents.repository.DocumentClassRepository;
import ru.mdm.documents.repository.DocumentPropertyRepository;
import ru.mdm.documents.util.ExceptionUtils;
import ru.mdm.documents.util.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DocumentClassServiceImpl implements DocumentClassService {

    private final DocumentClassRepository classRepository;
    private final DocumentPropertyRepository propertyRepository;
    private final DocumentClassMapper classMapper;
    private final DocumentPropertyMapper propertyMapper;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<DocumentClassDto> createClass(CreateDocumentClassDto dto) {
        return checkIsAdmin()
                .thenReturn(classMapper.toEntity(dto))
                .flatMap(classRepository::save) //TODO Добавить проверку на уже дублирующийся класс
                .map(classMapper::toDto)
                .flatMap(createdClass -> saveProperties(createdClass, dto.getProperties()))
                .as(transactionalOperator::transactional)
                .onErrorMap(ExceptionUtils.extExceptionMapper(ErrorCode.CANNOT_CREATE_CLASS.getText()));
    }

    @Override
    public Mono<DocumentClassDto> updateClass(UUID id, UpdateDocumentClassDto dto) {
        return checkIsAdmin()
                .then(classRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(ErrorCode.CLASS_NOT_FOUND.buildErrorText(id))))
                .map(clazz -> classMapper.update(clazz, dto))
                .flatMap(classRepository::save)
                .map(classMapper::toDto)
                .flatMap(clazz -> propertyRepository.deleteByClassId(id).thenReturn(clazz))
                .flatMap(clazz -> saveProperties(clazz, dto.getProperties()))
                .as(transactionalOperator::transactional)
                .onErrorMap(ExceptionUtils.extExceptionMapper(ErrorCode.CANNOT_UPDATE_CLASS.getText()));
    }

    @Override
    public Mono<DocumentClassDto> deleteClass(UUID id) {
        return checkIsAdmin()
                .then(findClass(id))
                .flatMap(clazz -> propertyRepository.deleteByClassId(id).thenReturn(clazz))
                .flatMap(clazz -> classRepository.deleteById(id).thenReturn(clazz))
                .as(transactionalOperator::transactional)
                .onErrorMap(ExceptionUtils.extExceptionMapper(ErrorCode.CANNOT_DELETE_CLASS.getText()));
    }

    @Override
    public Mono<DocumentClassDto> getDocumentClass(UUID id) {
        return findClass(id)
                .onErrorMap(ExceptionUtils.extExceptionMapper(ErrorCode.CANNOT_GET_CLASS.getText()));
    }

    @Override
    public Flux<DocumentClassDto> getClasses(Pageable pageable) {
        return classRepository.findAllBy(pageable)
                .map(classMapper::toDto)
                .flatMap(clazz -> propertyRepository.findByClassId(clazz.getId())
                        .map(propertyMapper::toDto)
                        .collectList()
                        .doOnNext(clazz::setProperties)
                        .thenReturn(clazz))
                .onErrorMap(ExceptionUtils.extExceptionMapper(ErrorCode.CANNOT_GET_CLASSES.getText()));
    }

    private Mono<Void> checkIsAdmin() {
        return Mono.empty(); //TODO поправить
//        return SecurityContextHolder.getUserRoles()
//                .filter("mdm-admin"::equals)
//                .switchIfEmpty(Mono.error(new ForbiddenException(ErrorCode.FORBIDDEN)))
//                .then();
    }

    private Mono<DocumentClassDto> saveProperties(DocumentClassDto dto, List<CreatePropertyDto> properties) {
        return Flux.fromIterable((properties == null) ? new ArrayList<>() : properties)
                .map(property -> propertyMapper.toEntity(property, dto.getId()))
                .flatMap(propertyRepository::save)
                .map(propertyMapper::toDto)
                .collectList()
                .doOnNext(dto::setProperties)
                .thenReturn(dto);
    }

    private Mono<DocumentClassDto> findClass(UUID classId) {
        return classRepository.findById(classId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(ErrorCode.CLASS_NOT_FOUND.buildErrorText(classId))))
                .map(classMapper::toDto)
                .flatMap(clazz -> propertyRepository.findByClassId(classId)
                        .map(propertyMapper::toDto)
                        .collectList()
                        .doOnNext(clazz::setProperties)
                        .thenReturn(clazz));
    }
}
