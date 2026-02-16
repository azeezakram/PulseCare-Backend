package com.pulsecare.backend.module.patient_queue.ws;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class PatientQueueEventPublisher {

    private final SimpMessagingTemplate messaging;

    public PatientQueueEventPublisher(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void publish(PatientQueueEvent event) {
        messaging.convertAndSend("/topic/queue", event);
    }
}
