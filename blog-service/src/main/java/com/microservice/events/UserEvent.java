package com.microservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    private Long userId;
    private String name;
    private String email;
    private EventType eventType;

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
}