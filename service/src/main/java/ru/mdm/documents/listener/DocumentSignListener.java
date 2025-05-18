package ru.mdm.documents.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.mdm.documents.repository.DocumentSignRepository;
import ru.mdm.kafka.model.MdmKafkaMessage;

import java.util.Map;
import java.util.UUID;

import static ru.mdm.kafka.model.MdmKafkaHeaders.EVENT_TYPE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentSignListener {

    private static final String SIGN_DOCUMENT_SUCCESS_EVENT = "mdm-signatures-service.SignDocument.Success";
    private static final String VERIFY_FAILED_EVENT = "mdm-signatures-service.DocumentVerify.Failed";

    private final DocumentSignRepository signRepository;

    @KafkaListener(topicPattern = "${mdm.signatures.service.topic}")
    public void handleSignDocument(@Payload MdmKafkaMessage<Object> message, @Header(EVENT_TYPE) String eventType) {
        if (SIGN_DOCUMENT_SUCCESS_EVENT.equals(eventType)) {
            handleSuccessSigning(message);
        } else if (VERIFY_FAILED_EVENT.equals(eventType)) {
            handleVerifyFailed(message);
        }
    }

    private void handleSuccessSigning(MdmKafkaMessage<Object> message) {
        log.info("Receiving success signing event");
        Map<String, Object> signData = (Map<String, Object>) message.getEventData();
        UUID versionId = UUID.fromString(signData.get("versionId").toString());
        String userLogin = signData.get("userLogin").toString();

        signRepository.findByDocumentIdAndUserLogin(versionId, userLogin)
                .map(sign -> {
                    sign.setSigned(true);
                    return sign;
                })
                .flatMap(signRepository::save)
                .doOnSuccess(sign -> log.info("Sign with id={} was updated", sign.getId()))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private void handleVerifyFailed(MdmKafkaMessage<Object> message) {
        log.info("Receiving failed sign verification event");
        Map<String, Object> signData = (Map<String, Object>) message.getEventData();
        UUID versionId = UUID.fromString(signData.get("versionId").toString());
        String userLogin = signData.get("userLogin").toString();

        signRepository.findByDocumentIdAndUserLogin(versionId, userLogin)
                .map(sign -> {
                    sign.setSigned(false);
                    return sign;
                })
                .flatMap(signRepository::save)
                .doOnSuccess(sign -> log.info("Sign with id={} was updated", sign.getId()))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
