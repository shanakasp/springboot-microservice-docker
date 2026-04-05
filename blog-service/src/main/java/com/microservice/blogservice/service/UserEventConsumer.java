package com.microservice.blogservice.service;

import com.microservice.blogservice.model.KnownUser;
import com.microservice.blogservice.repository.KnownUserRepository;
import com.microservice.events.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final KnownUserRepository knownUserRepository;

    @KafkaListener(topics = "${app.kafka.user-events-topic}")
    public void handleUserEvent(UserEvent event) {
        switch (event.getEventType()) {
            case CREATED, UPDATED -> upsertKnownUser(event);
            case DELETED -> knownUserRepository.deleteById(event.getUserId());
        }
    }

    private void upsertKnownUser(UserEvent event) {
        KnownUser knownUser = knownUserRepository.findById(event.getUserId())
                .orElseGet(KnownUser::new);

        knownUser.setId(event.getUserId());
        knownUser.setName(event.getName());
        knownUser.setEmail(event.getEmail());

        knownUserRepository.save(knownUser);
    }
}