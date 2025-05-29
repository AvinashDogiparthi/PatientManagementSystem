package com.pm.analytics_service.Kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) throws InvalidProtocolBufferException {
        PatientEvent patientEvent = PatientEvent.parseFrom(event);
        LOGGER.info("KafkaConsumer::consumeEvent -- Received Patient Event : [ PatientID = {}, PatientName = {}, PatientEmail = {}]",
                patientEvent.getPatientId(),patientEvent.getName(),patientEvent.getEmail());
    }
}
