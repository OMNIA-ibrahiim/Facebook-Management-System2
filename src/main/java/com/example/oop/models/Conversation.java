package com.example.oop.models;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private Integer id;
    private List<Integer> participants;
    private List<Message> messages;

    public Conversation(Integer id, List<Integer> participants) {
        this.id = id;
        this.participants = participants;
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    public Integer getId() {
        return id;
    }
}