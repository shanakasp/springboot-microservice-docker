package com.microservice.userservice.service;

import com.microservice.events.UserEvent;
import com.microservice.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.user-events-topic}")
    private String topicName;

    public void publishCreated(User user) {
        publish(user, UserEvent.EventType.CREATED);
    }

    public void publishUpdated(User user) {
        publish(user, UserEvent.EventType.UPDATED);
    }

    public void publishDeleted(User user) {
        publish(user, UserEvent.EventType.DELETED);
    }

    private void publish(User user, UserEvent.EventType eventType) {
        UserEvent event = new UserEvent(user.getId(), user.getName(), user.getEmail(), eventType);
        kafkaTemplate.send(topicName, String.valueOf(user.getId()), event);
    }
}