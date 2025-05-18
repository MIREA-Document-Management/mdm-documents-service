package ru.mdm.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import ru.mdm.acl.service.AccessRuleService;
import ru.mdm.documents.exception.BadRequestServerException;
import ru.mdm.documents.exception.ErrorCode;
import ru.mdm.documents.exception.ForbiddenException;
import ru.mdm.documents.model.AccessLevel;
import ru.mdm.documents.model.Action;
import ru.mdm.documents.model.PropertyType;
import ru.mdm.documents.model.dto.documents.CreateDocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentDto;
import ru.mdm.documents.model.dto.documents.DocumentSigner;
import ru.mdm.documents.model.dto.documents.UpdateDocumentDto;
import ru.mdm.documents.model.entity.Document;
import ru.mdm.documents.model.entity.DocumentProperty;
import ru.mdm.documents.model.entity.DocumentSign;
import ru.mdm.documents.model.mapper.DocumentMapper;
import ru.mdm.documents.repository.DocumentClassRepository;
import ru.mdm.documents.repository.DocumentPropertyRepository;
import ru.mdm.documents.repository.DocumentRepository;
import ru.mdm.documents.repository.DocumentSignRepository;
import ru.mdm.documents.util.SecurityContextHolder;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentClassRepository classRepository;
    private final DocumentPropertyRepository propertyRepository;
    private final DocumentSignRepository signRepository;
    private final AccessRuleService accessService;
    private final TransactionalOperator txOperator;

    @Override
    public Mono<DocumentDto> createDocument(CreateDocumentDto dto) {
        return Mono.just(dto)
                .flatMap(docDto -> validateDocumentData(docDto.getClassName(), docDto.getData())
                        .doOnNext(docDto::setData)
                        .thenReturn(docDto))
                .map(documentMapper::toEntity)
                .map(initDocumentIdAndVersionId())
                .flatMap(documentRepository::save)
                .map(documentMapper::toDto)
                .flatMap(documentDto -> createAndFillSigners(documentDto, dto))
                .as(txOperator::transactional);
    }

    @Override
    public Mono<DocumentDto> createDocumentVersion(UUID id, CreateDocumentDto dto) {
        return documentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestServerException(ErrorCode.DOCUMENT_NOT_FOUND.buildErrorText(id)))))
                .flatMap(document -> checkRights(document, Action.CREATE_VERSION))
                .thenReturn(dto)
                .flatMap(docDto -> validateDocumentData(docDto.getClassName(), docDto.getData())
                        .doOnNext(docDto::setData)
                        .thenReturn(docDto))
                .map(documentMapper::toEntity)
                .map(initVersionId(id))
                .flatMap(documentRepository::save)
                .map(documentMapper::toDto);
    }

    @Override
    public Mono<DocumentDto> updateDocument(UUID id, UUID versionId, UpdateDocumentDto dto) {
        return documentRepository.findByIdAndVersionId(id, versionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestServerException(ErrorCode.VERSION_NOT_FOUND.buildErrorText(versionId)))))
                .flatMap(document -> checkRights(document, Action.UPDATE).thenReturn(document))
                .flatMap(document -> validateDocumentData(document.getClassName(), dto.getData())
                        .doOnNext(dto::setData)
                        .thenReturn(document))
                .map(document -> documentMapper.update(document, dto))
                .flatMap(documentRepository::save)
                .map(documentMapper::toDto)
                .flatMap(documentDto -> updateAndFillSigners(documentDto, dto))
                .as(txOperator::transactional);
    }

    @Override
    public Mono<DocumentDto> getDocument(UUID id, UUID versionId) {
        return documentRepository.findByIdAndVersionId(id, versionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestServerException(ErrorCode.VERSION_NOT_FOUND.buildErrorText(versionId)))))
                .flatMap(document -> checkRights(document, Action.READ).thenReturn(document))
                .map(documentMapper::toDto)
                .flatMap(this::fillSigners);
    }

    @Override
    public Flux<DocumentDto> getDocuments(Pageable pageable) {
        return documentRepository.findAllHeadDocuments(pageable)
                .flatMap(document -> checkRights(document, Action.READ)
                        .thenReturn(document)
                        .onErrorResume((throwable -> Mono.empty())))
                .map(documentMapper::toDto);
    }

    @Override
    public Flux<DocumentDto> getDocumentVersions(UUID id, Pageable pageable) {
        return documentRepository.findAllDocumentVersions(id, pageable)
                .flatMap(document -> checkRights(document, Action.READ)
                        .thenReturn(document)
                        .onErrorResume((throwable -> Mono.empty())))
                .map(documentMapper::toDto);
    }

    @Override
    public Mono<DocumentDto> deleteDocumentVersion(UUID id, UUID versionId) {
        return documentRepository.findByIdAndVersionId(id, versionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestServerException(ErrorCode.VERSION_NOT_FOUND.buildErrorText(versionId)))))
                .flatMap(document -> checkRights(document, Action.DELETE).thenReturn(document))
                .flatMap(document -> signRepository.deleteByDocumentId(versionId).thenReturn(document))
                .flatMap(document -> documentRepository.deleteById(id).thenReturn(document))
                .map(documentMapper::toDto);
    }

    @Override
    public Mono<Void> deleteDocumentWithAllVersions(UUID id) {
        return documentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestServerException(ErrorCode.DOCUMENT_NOT_FOUND.buildErrorText(id)))))
                .flatMap(document -> checkRights(document, Action.DELETE).thenReturn(document))
                .flatMap(document -> getDocumentVersions(id, PageRequest.of(0, 1000))
                        .flatMap(docVersion -> signRepository.deleteByDocumentId(docVersion.getVersionId()))
                        .then(Mono.just(document)))
                .flatMap(document -> documentRepository.deleteAllDocumentVersions(id));
    }

    private static Function<Document, Document> initDocumentIdAndVersionId() {
        return document -> {
            var docId = UUID.randomUUID();
            document.setId(docId);
            document.setVersionId(docId);
            return document;
        };
    }

    private static Function<Document, Document> initVersionId(UUID id) {
        return document -> {
            document.setId(id);
            document.setVersionId(UUID.randomUUID());
            return document;
        };
    }

    private Mono<Void> checkRights(Document document, Action action) {
        if (AccessLevel.PUBLIC.equals(document.getAccessLevel())) {
            return Mono.empty();
        } else if (AccessLevel.PRIVATE.equals(document.getAccessLevel())) {
            return SecurityContextHolder.getUserLogin()
                    .zipWith(SecurityContextHolder.isAdmin())
                    .flatMap(objects -> {
                        var login = objects.getT1();
                        var isAdmin = objects.getT2();

                        if (isAdmin) {
                            return Mono.empty();
                        }
                        if (document.getCreatedBy().equals(login)) {
                            return Mono.empty();
                        }
                        return Mono.error(new ForbiddenException(ErrorCode.FORBIDDEN));
                    });
        } else {
            return SecurityContextHolder.isAdmin()
                    .flatMap(isAdmin -> {
                        if (isAdmin) {
                            return Mono.empty();
                        }
                        return accessService.checkPermission(document.getVersionId(), action.name());
                    });
        }
    }

    private Mono<Map<String, Object>> validateDocumentData(String className, Map<String, Object> docData) {
        return classRepository.findByClassName(className)
                .switchIfEmpty(Mono.error(new BadRequestServerException(ErrorCode.CLASS_NAME_NOT_FOUND.buildErrorText(className))))
                .flatMap(clazz -> propertyRepository.findByClassId(clazz.getId()).collectList())
                .handle(checkRequiredProps(docData))
                .flatMap(classProps -> filterProperties(classProps, docData));
    }

    private BiConsumer<List<DocumentProperty>, SynchronousSink<List<DocumentProperty>>> checkRequiredProps(
            Map<String, Object> docProperties) {
        return (classProperties, sink) -> {
            var missingProps = new ArrayList<DocumentProperty>();

            classProperties.stream()
                    .filter(DocumentProperty::getRequired)
                    .forEach(clazz -> {
                        if (!docProperties.containsKey(clazz.getCode())) {
                            missingProps.add(clazz);
                        }
                    });

            if (missingProps.isEmpty()) {
                sink.next(classProperties);
            } else {
                var message = missingProps.stream()
                        .map(DocumentProperty::getName)
                        .collect(Collectors.joining(", "));

                sink.error(new BadRequestServerException("Отсутствуют обязательные атрибуты документа: " + message));
            }
        };
    }

    private Mono<Map<String, Object>> filterProperties(List<DocumentProperty> classProperties, Map<String, Object> docData) {
        return Mono.justOrEmpty(docData) //TODO уточнить
                .map(data -> {
                    var allowedCodes = classProperties.stream().map(DocumentProperty::getCode).toList();
                    var resultProps = new HashMap<String, Object>();

                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        if (allowedCodes.contains(entry.getKey())) {
                            var documentProperty = classProperties.stream()
                                    .filter(property -> entry.getKey().equals(property.getCode()))
                                    .findFirst()
                                    .get();

                            validateType(entry.getValue(), documentProperty.getType(), entry.getKey());
                            resultProps.put(entry.getKey(), entry.getValue());
                        }
                    }
                    return resultProps;
                });
    }

    private Mono<DocumentDto> createAndFillSigners(DocumentDto documentDto, CreateDocumentDto dto) {
        if (dto.getUsersToSign() == null || dto.getUsersToSign().isEmpty()) {
            return Mono.just(documentDto);
        }
        return Flux.fromIterable(new HashSet<>(dto.getUsersToSign()))
                .map(login -> {
                    var entity = new DocumentSign();
                    entity.setDocumentId(documentDto.getVersionId());
                    entity.setUserLogin(login);
                    return entity;
                })
                .flatMap(signRepository::save)
                .map(sign -> new DocumentSigner(sign.getUserLogin(), sign.getSigned()))
                .collectList()
                .doOnNext(documentDto::setSigners)
                .thenReturn(documentDto);
    }

    private Mono<DocumentDto> updateAndFillSigners(DocumentDto documentDto, UpdateDocumentDto dto) {
        if (dto.getUsersToSign() == null || dto.getUsersToSign().isEmpty()) {
            return signRepository.deleteByDocumentId(documentDto.getVersionId())
                    .thenReturn(documentDto);
        }
        return signRepository.findByDocumentId(documentDto.getVersionId())
                .flatMap(sign -> {
                    if (dto.getUsersToSign().contains(sign.getUserLogin())) {
                        return Mono.just(sign);
                    }
                    return signRepository.deleteById(sign.getId()).then(Mono.empty());
                })
                .collectList()
                .flatMap(existingSigners -> Flux.fromIterable(new HashSet<>(dto.getUsersToSign()))
                        .filter(login -> existingSigners.stream().noneMatch(sign -> sign.getUserLogin().equals(login)))
                        .map(login -> {
                            var entity = new DocumentSign();
                            entity.setDocumentId(documentDto.getVersionId());
                            entity.setUserLogin(login);
                            return entity;
                        })
                        .flatMap(signRepository::save)
                        .collectList()
                        .doOnNext(existingSigners::addAll)
                        .thenReturn(existingSigners))
                .flatMapMany(Flux::fromIterable)
                .map(sign -> new DocumentSigner(sign.getUserLogin(), sign.getSigned()))
                .collectList()
                .doOnNext(documentDto::setSigners)
                .thenReturn(documentDto);
    }

    private Mono<DocumentDto> fillSigners(DocumentDto documentDto) {
        return signRepository.findByDocumentId(documentDto.getVersionId())
                .map(sign -> new DocumentSigner(sign.getUserLogin(), sign.getSigned()))
                .collectList()
                .doOnNext(documentDto::setSigners)
                .thenReturn(documentDto);
    }

    private void validateType(Object value, PropertyType type, String code) {
        switch (type) {
            case TEXT -> validateTextType(value, code);
            case DOUBLE -> validateDoubleType(value, code);
            case INTEGER -> validateIntegerType(value, code);
            case BOOLEAN -> validateBooleanType(value, code);
            case DATETIME -> validateDatetimeType(value, code);
        }
    }

    private void validateTextType(Object value, String code) {
        if (!(value instanceof String)) {
            throw new BadRequestServerException(ErrorCode.INCORRECT_TYPE.buildErrorText(code, PropertyType.TEXT.name()));
        }
    }

    private void validateDoubleType(Object value, String code) {
        if (!(value instanceof Double)) {
            throw new BadRequestServerException(ErrorCode.INCORRECT_TYPE.buildErrorText(code, PropertyType.DOUBLE.name()));
        }
    }

    private void validateIntegerType(Object value, String code) {
        if (!(value instanceof Integer)) {
            throw new BadRequestServerException(ErrorCode.INCORRECT_TYPE.buildErrorText(code, PropertyType.INTEGER.name()));
        }
    }

    private void validateBooleanType(Object value, String code) {
        if (!(value instanceof Boolean)) {
            throw new BadRequestServerException(ErrorCode.INCORRECT_TYPE.buildErrorText(code, PropertyType.BOOLEAN.name()));
        }
    }

    private void validateDatetimeType(Object value, String code) {
        //TODO реализовать
    }
}
